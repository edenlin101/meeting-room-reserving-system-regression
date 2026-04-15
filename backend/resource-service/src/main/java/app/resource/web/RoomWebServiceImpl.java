package app.resource.web;

import app.resource.api.room.ListRoomRequest;
import app.resource.api.room.ListRoomResponse;
import app.resource.api.room.RoomWebService;
import app.resource.service.RoomService;
import core.framework.inject.Inject;

public class RoomWebServiceImpl implements RoomWebService {
    @Inject
    RoomService roomService;

    @Override
    public ListRoomResponse list(ListRoomRequest request) {
        return roomService.list(request);
    }
}
