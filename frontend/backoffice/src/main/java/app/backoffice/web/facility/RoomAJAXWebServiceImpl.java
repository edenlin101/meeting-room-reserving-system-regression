package app.backoffice.web.facility;

import app.facility.api.room.CreateRoomRequest;
import app.facility.api.room.CreateRoomResponse;
import app.facility.api.room.ListRoomResponse;
import app.facility.api.room.RoomWebService;
import app.backoffice.api.facility.CreateRoomAJAXRequest;
import app.backoffice.api.facility.CreateRoomAJAXResponse;
import app.backoffice.api.facility.ListRoomAJAXResponse;
import app.backoffice.api.facility.RoomAJAXView;
import app.backoffice.api.facility.RoomAJAXWebService;
import core.framework.inject.Inject;

import java.util.stream.Collectors;

public class RoomAJAXWebServiceImpl implements RoomAJAXWebService {
    @Inject
    RoomWebService roomWebService;

    @Override
    public CreateRoomAJAXResponse create(CreateRoomAJAXRequest req) {
        CreateRoomRequest remoteReq = new CreateRoomRequest();
        remoteReq.name = req.name;
        remoteReq.companyId = req.companyId;

        CreateRoomResponse remoteResp = roomWebService.create(remoteReq);

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
        ListRoomResponse remoteResp = roomWebService.list(null);

        ListRoomAJAXResponse resp = new ListRoomAJAXResponse();
        resp.items = remoteResp.items.stream().map(r -> {
            RoomAJAXView view = new RoomAJAXView();
            view.id = r.id;
            view.name = r.name;
            view.companyId = r.companyId;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}