package app.reservation.service;

import app.reservation.api.reservation.CancelReservationRequest;
import app.reservation.api.reservation.CreateReservationRequest;
import app.reservation.api.reservation.CreateReservationResponse;
import app.reservation.api.reservation.ReservationStatusView;
import app.reservation.api.reservation.ReservationView;
import app.reservation.api.reservation.SearchAvailabilityRequest;
import app.reservation.api.reservation.SearchAvailabilityResponse;
import app.reservation.api.reservation.SearchReservationRequest;
import app.reservation.api.reservation.SearchReservationResponse;
import app.reservation.api.reservation.TimeSlotView;
import app.reservation.domain.Reservation;
import app.reservation.domain.ReservationStatus;
import app.reservation.domain.ReservationTracking;
import app.reservation.domain.TrackingAction;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.mongo.MongoCollection;
import core.framework.web.exception.ConflictException;
import core.framework.web.exception.NotFoundException;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    @Inject
    Repository<Reservation> reservationRepository;

    @Inject
    MongoCollection<ReservationTracking> trackingCollection;

    public SearchAvailabilityResponse searchAvailability(SearchAvailabilityRequest request) {
        List<Reservation> existing = reservationRepository.select("room_id = ? AND date = ? AND status = 'RESERVED'", request.roomId, request.date);
        
        List<TimeSlotView> timeSlots = new ArrayList<>();
        LocalTime current = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        while (current.isBefore(end)) {
            LocalTime next = current.plusMinutes(30);
            boolean isReserved = false;
            for (Reservation r : existing) {
                if (!r.startTime.isAfter(current) && r.endTime.isAfter(current)) {
                    isReserved = true;
                    break;
                }
            }

            TimeSlotView slot = new TimeSlotView();
            slot.startTime = current;
            slot.endTime = next;
            slot.isAvailable = !isReserved;
            timeSlots.add(slot);

            current = next;
        }

        SearchAvailabilityResponse response = new SearchAvailabilityResponse();
        response.timeSlots = timeSlots;
        return response;
    }

    public CreateReservationResponse create(CreateReservationRequest request) {
        // Simple check for overlap
        if (!reservationRepository.select("room_id = ? AND date = ? AND status = 'RESERVED' AND ((start_time <= ? AND end_time > ?) OR (start_time < ? AND end_time >= ?))",
            request.roomId, request.date, request.startTime, request.startTime, request.endTime, request.endTime).isEmpty()) {
            throw new ConflictException("time slot is already reserved");
        }

        Reservation reservation = new Reservation();
        reservation.userId = request.userId;
        reservation.companyId = request.companyId;
        reservation.roomId = request.roomId;
        reservation.date = request.date;
        reservation.startTime = request.startTime;
        reservation.endTime = request.endTime;
        reservation.status = ReservationStatus.RESERVED;
        reservation.createdAt = ZonedDateTime.now();

        reservation.id = reservationRepository.insert(reservation).orElseThrow();

        // Save tracking to mongo
        ReservationTracking tracking = new ReservationTracking();
        tracking.id = new ObjectId();
        tracking.reservationId = reservation.id;
        tracking.action = TrackingAction.CREATE;
        tracking.userId = request.userId;
        tracking.timestamp = ZonedDateTime.now();
        trackingCollection.insert(tracking);

        CreateReservationResponse response = new CreateReservationResponse();
        response.id = reservation.id;
        return response;
    }

    public void cancel(Long id, CancelReservationRequest request) {
        Reservation reservation = reservationRepository.get(id).orElseThrow(() -> new NotFoundException("reservation not found"));
        if (!reservation.userId.equals(request.userId)) {
            throw new ConflictException("can only cancel own reservations");
        }
        reservation.status = ReservationStatus.CANCELED;
        reservationRepository.partialUpdate(reservation);

        ReservationTracking tracking = new ReservationTracking();
        tracking.id = new ObjectId();
        tracking.reservationId = reservation.id;
        tracking.action = TrackingAction.CANCEL;
        tracking.userId = request.userId;
        tracking.timestamp = ZonedDateTime.now();
        trackingCollection.insert(tracking);
    }

    public SearchReservationResponse search(SearchReservationRequest request) {
        StringBuilder query = new StringBuilder("1=1");
        List<Object> params = new ArrayList<>();
        
        if (request != null) {
            if (request.companyId != null) {
                query.append(" AND company_id = ?");
                params.add(request.companyId);
            }
            if (request.roomId != null) {
                query.append(" AND room_id = ?");
                params.add(request.roomId);
            }
            if (request.date != null) {
                query.append(" AND date = ?");
                params.add(request.date);
            }
        }

        List<Reservation> list = reservationRepository.select(query.toString(), params.toArray());

        SearchReservationResponse response = new SearchReservationResponse();
        response.reservations = list.stream().map(r -> {
            ReservationView view = new ReservationView();
            view.id = r.id;
            view.userId = r.userId;
            view.companyId = r.companyId;
            view.roomId = r.roomId;
            view.date = r.date;
            view.startTime = r.startTime;
            view.endTime = r.endTime;
            view.status = r.status == ReservationStatus.RESERVED ? ReservationStatusView.RESERVED : ReservationStatusView.CANCELED;
            return view;
        }).collect(Collectors.toList());

        return response;
    }
}