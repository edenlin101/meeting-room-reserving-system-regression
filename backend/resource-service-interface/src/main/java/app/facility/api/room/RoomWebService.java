package app.facility.api.room;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface RoomWebService {
    @POST
    @Path("/room")
    @ResponseStatus(HTTPStatus.CREATED)
    CreateRoomResponse create(CreateRoomRequest request);

    @DELETE
    @Path("/room/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/room/list")
    ListRoomResponse list(ListRoomRequest request);
}