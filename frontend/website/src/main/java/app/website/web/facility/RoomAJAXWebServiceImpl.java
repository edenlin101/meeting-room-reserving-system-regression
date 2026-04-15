package app.website.web.facility;

import app.facility.api.room.ListRoomRequest;
import app.facility.api.room.ListRoomResponse;
import app.facility.api.room.RoomWebService;
import app.website.api.facility.ListRoomAJAXRequest;
import app.website.api.facility.ListRoomAJAXResponse;
import app.website.api.facility.RoomAJAXView;
import app.website.api.facility.RoomAJAXWebService;
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
        resp.items = remoteResp.items.stream().map(r -> {
            RoomAJAXView view = new RoomAJAXView();
            view.id = r.id;
            view.name = r.name;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}