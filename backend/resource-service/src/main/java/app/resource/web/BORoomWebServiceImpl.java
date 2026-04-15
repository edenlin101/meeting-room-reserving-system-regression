package app.resource.web;

import app.resource.api.room.BORoomWebService;
import app.resource.api.room.BOCreateRoomRequest;
import app.resource.api.room.BOCreateRoomResponse;
import app.resource.api.room.BOListRoomRequest;
import app.resource.api.room.BOListRoomResponse;
import app.resource.api.room.CreateRoomRequest;
import app.resource.api.room.CreateRoomResponse;
import app.resource.api.room.ListRoomRequest;
import app.resource.api.room.ListRoomResponse;
import app.resource.service.RoomService;
import core.framework.inject.Inject;

public class BORoomWebServiceImpl implements BORoomWebService {
    @Inject
    RoomService roomService;

    @Override
    public BOCreateRoomResponse create(BOCreateRoomRequest request) {
        CreateRoomRequest req = new CreateRoomRequest();
        req.name = request.name;
        req.companyId = request.companyId;
        
        CreateRoomResponse resp = roomService.create(req);
        
        BOCreateRoomResponse boResp = new BOCreateRoomResponse();
        boResp.id = resp.id;
        boResp.name = resp.name;
        boResp.companyId = resp.companyId;
        return boResp;
    }

    @Override
    public void delete(Long id) {
        roomService.delete(id);
    }

    @Override
    public BOListRoomResponse list(BOListRoomRequest request) {
        ListRoomRequest req = new ListRoomRequest();
        if (request != null) req.companyId = request.companyId;
        
        ListRoomResponse resp = roomService.list(req);
        
        BOListRoomResponse boResp = new BOListRoomResponse();
        boResp.rooms = resp.rooms;
        return boResp;
    }
}
