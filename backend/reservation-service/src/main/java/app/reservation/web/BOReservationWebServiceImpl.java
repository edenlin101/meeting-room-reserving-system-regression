package app.reservation.web;

import app.reservation.api.reservation.BOReservationWebService;
import app.reservation.api.reservation.BOSearchReservationRequest;
import app.reservation.api.reservation.BOSearchReservationResponse;
import app.reservation.api.reservation.SearchReservationRequest;
import app.reservation.api.reservation.SearchReservationResponse;
import app.reservation.service.ReservationService;
import core.framework.inject.Inject;

public class BOReservationWebServiceImpl implements BOReservationWebService {
    @Inject
    ReservationService reservationService;

    @Override
    public BOSearchReservationResponse search(BOSearchReservationRequest request) {
        SearchReservationRequest req = new SearchReservationRequest();
        req.companyId = request.companyId;
        req.roomId = request.roomId;
        req.date = request.date;
        
        SearchReservationResponse resp = reservationService.search(req);
        
        BOSearchReservationResponse boResp = new BOSearchReservationResponse();
        boResp.reservations = resp.reservations;
        return boResp;
    }
}
