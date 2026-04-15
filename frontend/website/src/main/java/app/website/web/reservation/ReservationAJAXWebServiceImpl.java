package app.website.web.reservation;

import app.reservation.api.reservation.CancelReservationRequest;
import app.reservation.api.reservation.CreateReservationRequest;
import app.reservation.api.reservation.CreateReservationResponse;
import app.reservation.api.reservation.ReservationWebService;
import app.reservation.api.reservation.SearchAvailabilityRequest;
import app.reservation.api.reservation.SearchAvailabilityResponse;
import app.website.api.reservation.ReservationAJAXWebService;
import app.website.api.reservation.ReserveRoomAJAXRequest;
import app.website.api.reservation.ReserveRoomAJAXResponse;
import app.website.api.reservation.SearchAvailabilityAJAXRequest;
import app.website.api.reservation.SearchAvailabilityAJAXResponse;
import app.website.api.reservation.TimeSlotAJAXView;
import core.framework.inject.Inject;
import core.framework.web.Request;
import core.framework.web.exception.UnauthorizedException;

import java.util.stream.Collectors;

public class ReservationAJAXWebServiceImpl implements ReservationAJAXWebService {
    @Inject
    ReservationWebService reservationWebService;

    @Inject
    Request request;

    private Long currentUserId() {
        return Long.parseLong(request.session().get("userId").orElseThrow(() -> new UnauthorizedException("missing session")));
    }

    private Long currentCompanyId() {
        return Long.parseLong(request.session().get("companyId").orElseThrow(() -> new UnauthorizedException("missing session")));
    }

    @Override
    public SearchAvailabilityAJAXResponse searchAvailability(SearchAvailabilityAJAXRequest req) {
        SearchAvailabilityRequest remoteReq = new SearchAvailabilityRequest();
        remoteReq.roomId = req.roomId;
        remoteReq.date = req.date;

        SearchAvailabilityResponse remoteResp = reservationWebService.searchAvailability(remoteReq);

        SearchAvailabilityAJAXResponse resp = new SearchAvailabilityAJAXResponse();
        resp.timeSlots = remoteResp.timeSlots.stream().map(t -> {
            TimeSlotAJAXView view = new TimeSlotAJAXView();
            view.startTime = t.startTime;
            view.endTime = t.endTime;
            view.isAvailable = t.isAvailable;
            return view;
        }).collect(Collectors.toList());

        return resp;
    }

    @Override
    public ReserveRoomAJAXResponse reserve(ReserveRoomAJAXRequest req) {
        CreateReservationRequest remoteReq = new CreateReservationRequest();
        remoteReq.userId = currentUserId();
        remoteReq.companyId = currentCompanyId();
        remoteReq.roomId = req.roomId;
        remoteReq.date = req.date;
        remoteReq.startTime = req.startTime;
        remoteReq.endTime = req.endTime;

        CreateReservationResponse remoteResp = reservationWebService.create(remoteReq);

        ReserveRoomAJAXResponse resp = new ReserveRoomAJAXResponse();
        resp.id = remoteResp.id;
        return resp;
    }

    @Override
    public void cancel(Long id) {
        CancelReservationRequest remoteReq = new CancelReservationRequest();
        remoteReq.userId = currentUserId();
        reservationWebService.cancel(id, remoteReq);
    }
}