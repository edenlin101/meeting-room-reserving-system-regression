package app.backoffice.api.reservation;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface ReservationAJAXWebService {
    @GET
    @Path("/ajax/reservation/list")
    SearchReservationAJAXResponse search(SearchReservationAJAXRequest request);
}