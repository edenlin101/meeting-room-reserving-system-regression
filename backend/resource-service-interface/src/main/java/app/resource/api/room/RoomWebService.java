package app.resource.api.room;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface RoomWebService {
    @GET
    @Path("/room/list")
    ListRoomResponse list(ListRoomRequest request);
}
