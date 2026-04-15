package app.facility.web;

import app.facility.api.room.CreateRoomRequest;
import app.facility.api.room.CreateRoomResponse;
import app.facility.api.room.ListRoomRequest;
import app.facility.api.room.ListRoomResponse;
import app.facility.api.room.RoomWebService;
import app.facility.service.RoomService;
import core.framework.inject.Inject;

public class RoomWebServiceImpl implements RoomWebService {
    @Inject
    RoomService roomService;

    @Override
    public CreateRoomResponse create(CreateRoomRequest request) {
        return roomService.create(request);
    }

    @Override
    public void delete(Long id) {
        roomService.delete(id);
    }

    @Override
    public ListRoomResponse list(ListRoomRequest request) {
        return roomService.list(request);
    }
}