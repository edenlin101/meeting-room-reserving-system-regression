package app.backoffice.api.facility;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface RoomAJAXWebService {
    @POST
    @Path("/ajax/room")
    @ResponseStatus(HTTPStatus.CREATED)
    CreateRoomAJAXResponse create(CreateRoomAJAXRequest request);

    @DELETE
    @Path("/ajax/room/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/ajax/room/list")
    ListRoomAJAXResponse list();
}