package app.website.web.facility;

import app.resource.api.room.ListRoomRequest;
import app.resource.api.room.ListRoomResponse;
import app.resource.api.room.RoomWebService;
import app.website.api.room.ListRoomAJAXRequest;
import app.website.api.room.ListRoomAJAXResponse;
import app.website.api.room.RoomAJAXView;
import app.website.api.room.RoomAJAXWebService;
import core.framework.inject.Inject;
import java.util.stream.Collectors;

public class RoomAJAXWebServiceImpl implements RoomAJAXWebService {
    @Inject
    RoomWebService roomWebService;

    @Override
    public ListRoomAJAXResponse list(ListRoomAJAXRequest req) {
        ListRoomRequest remoteReq = new ListRoomRequest();
        remoteReq.companyId = req.companyId;

        ListRoomResponse remoteResp = roomWebService.list(remoteReq);

        ListRoomAJAXResponse resp = new ListRoomAJAXResponse();
        resp.rooms = remoteResp.rooms.stream().map(r -> {
            RoomAJAXView view = new RoomAJAXView();
            view.id = r.id;
            view.name = r.name;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}