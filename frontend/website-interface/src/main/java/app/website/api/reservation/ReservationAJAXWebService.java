package app.website.api.reservation;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface ReservationAJAXWebService {
    @GET
    @Path("/ajax/room/availability")
    SearchAvailabilityAJAXResponse searchAvailability(SearchAvailabilityAJAXRequest request);

    @POST
    @Path("/ajax/reservation")
    @ResponseStatus(HTTPStatus.CREATED)
    ReserveRoomAJAXResponse reserve(ReserveRoomAJAXRequest request);

    @PUT
    @Path("/ajax/reservation/:id/cancel")
    void cancel(@PathParam("id") Long id);
}