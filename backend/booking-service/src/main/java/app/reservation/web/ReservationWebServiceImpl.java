package app.reservation.web;

import app.reservation.api.reservation.CancelReservationRequest;
import app.reservation.api.reservation.CreateReservationRequest;
import app.reservation.api.reservation.CreateReservationResponse;
import app.reservation.api.reservation.ReservationWebService;
import app.reservation.api.reservation.SearchAvailabilityRequest;
import app.reservation.api.reservation.SearchAvailabilityResponse;
import app.reservation.api.reservation.SearchReservationRequest;
import app.reservation.api.reservation.SearchReservationResponse;
import app.reservation.service.ReservationService;
import core.framework.inject.Inject;

public class ReservationWebServiceImpl implements ReservationWebService {
    @Inject
    ReservationService reservationService;

    @Override
    public SearchAvailabilityResponse searchAvailability(SearchAvailabilityRequest request) {
        return reservationService.searchAvailability(request);
    }

    @Override
    public CreateReservationResponse create(CreateReservationRequest request) {
        return reservationService.create(request);
    }

    @Override
    public void cancel(Long id, CancelReservationRequest request) {
        reservationService.cancel(id, request);
    }

    @Override
    public SearchReservationResponse search(SearchReservationRequest request) {
        return reservationService.search(request);
    }
}