package app.reservation.api.reservation;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface BOReservationWebService {
    @GET
    @Path("/bo/reservation/list")
    BOSearchReservationResponse search(BOSearchReservationRequest request);
}
