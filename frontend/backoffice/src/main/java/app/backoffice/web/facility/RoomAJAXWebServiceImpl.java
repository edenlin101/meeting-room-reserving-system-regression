package app.backoffice.web.facility;

import app.resource.api.room.BORoomWebService;
import app.resource.api.room.BOCreateRoomRequest;
import app.resource.api.room.BOCreateRoomResponse;
import app.resource.api.room.BOListRoomRequest;
import app.resource.api.room.BOListRoomResponse;
import app.backoffice.api.room.CreateRoomAJAXRequest;
import app.backoffice.api.room.CreateRoomAJAXResponse;
import app.backoffice.api.room.ListRoomAJAXResponse;
import app.backoffice.api.room.RoomAJAXView;
import app.backoffice.api.room.RoomAJAXWebService;
import core.framework.inject.Inject;
import java.util.stream.Collectors;

public class RoomAJAXWebServiceImpl implements RoomAJAXWebService {
    @Inject
    BORoomWebService roomWebService;

    @Override
    public CreateRoomAJAXResponse create(CreateRoomAJAXRequest req) {
        BOCreateRoomRequest remoteReq = new BOCreateRoomRequest();
        remoteReq.name = req.name;
        remoteReq.companyId = req.companyId;

        BOCreateRoomResponse remoteResp = roomWebService.create(remoteReq);

        CreateRoomAJAXResponse resp = new CreateRoomAJAXResponse();
        resp.id = remoteResp.id;
        resp.name = remoteResp.name;
        resp.companyId = remoteResp.companyId;
        return resp;
    }

    @Override
    public void delete(Long id) {
        roomWebService.delete(id);
    }

    @Override
    public ListRoomAJAXResponse list() {
        BOListRoomResponse remoteResp = roomWebService.list(null);

        ListRoomAJAXResponse resp = new ListRoomAJAXResponse();
        resp.rooms = remoteResp.rooms.stream().map(r -> {
            RoomAJAXView view = new RoomAJAXView();
            view.id = r.id;
            view.name = r.name;
            view.companyId = r.companyId;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}
