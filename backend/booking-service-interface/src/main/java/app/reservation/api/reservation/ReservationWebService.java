package app.reservation.api.reservation;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface ReservationWebService {
    @GET
    @Path("/room/availability")
    SearchAvailabilityResponse searchAvailability(SearchAvailabilityRequest request);

    @POST
    @Path("/reservation")
    @ResponseStatus(HTTPStatus.CREATED)
    CreateReservationResponse create(CreateReservationRequest request);

    @PUT
    @Path("/reservation/:id/cancel")
    void cancel(@PathParam("id") Long id, CancelReservationRequest request);

    @GET
    @Path("/reservation/list")
    SearchReservationResponse search(SearchReservationRequest request);
}