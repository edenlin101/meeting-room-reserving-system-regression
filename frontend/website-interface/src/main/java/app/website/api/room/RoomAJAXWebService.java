package app.website.api.room;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.QueryParam;

public interface RoomAJAXWebService {
    @GET
    @Path("/ajax/room/list")
    ListRoomAJAXResponse list(ListRoomAJAXRequest request);
}