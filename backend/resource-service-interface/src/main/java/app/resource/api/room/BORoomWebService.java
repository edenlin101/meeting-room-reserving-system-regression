package app.resource.api.room;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface BORoomWebService {
    @POST
    @Path("/bo/room")
    @ResponseStatus(HTTPStatus.CREATED)
    BOCreateRoomResponse create(BOCreateRoomRequest request);

    @DELETE
    @Path("/bo/room/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/bo/room/list")
    BOListRoomResponse list(BOListRoomRequest request);
}
