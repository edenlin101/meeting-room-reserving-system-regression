package app.backoffice.web.reservation;

import app.reservation.api.reservation.BOReservationWebService;
import app.reservation.api.reservation.BOSearchReservationRequest;
import app.reservation.api.reservation.BOSearchReservationResponse;
import app.backoffice.api.reservation.ReservationAJAXView;
import app.backoffice.api.reservation.ReservationAJAXWebService;
import app.backoffice.api.reservation.ReservationStatusAJAXView;
import app.backoffice.api.reservation.SearchReservationAJAXRequest;
import app.backoffice.api.reservation.SearchReservationAJAXResponse;
import core.framework.inject.Inject;

import java.util.stream.Collectors;

public class ReservationAJAXWebServiceImpl implements ReservationAJAXWebService {
    @Inject
    BOReservationWebService reservationWebService;

    @Override
    public SearchReservationAJAXResponse search(SearchReservationAJAXRequest req) {
        BOSearchReservationRequest remoteReq = new BOSearchReservationRequest();
        remoteReq.companyId = req.companyId;
        remoteReq.roomId = req.roomId;
        remoteReq.date = req.date;

        BOSearchReservationResponse remoteResp = reservationWebService.search(remoteReq);

        SearchReservationAJAXResponse resp = new SearchReservationAJAXResponse();
        resp.reservations = remoteResp.reservations.stream().map(r -> {
            ReservationAJAXView view = new ReservationAJAXView();
            view.id = r.id;
            view.userId = r.userId;
            view.companyId = r.companyId;
            view.roomId = r.roomId;
            view.date = r.date;
            view.startTime = r.startTime;
            view.endTime = r.endTime;
            view.status = ReservationStatusAJAXView.valueOf(r.status.name());
            return view;
        }).collect(Collectors.toList());

        return resp;
    }
}
