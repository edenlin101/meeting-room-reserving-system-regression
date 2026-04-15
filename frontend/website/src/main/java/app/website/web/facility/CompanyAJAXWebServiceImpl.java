package app.website.web.facility;

import app.facility.api.company.CompanyWebService;
import app.facility.api.company.ListCompanyResponse;
import app.facility.api.room.ListRoomRequest;
import app.facility.api.room.ListRoomResponse;
import app.facility.api.room.RoomWebService;
import app.website.api.facility.CompanyAJAXView;
import app.website.api.facility.CompanyAJAXWebService;
import app.website.api.facility.ListCompanyAJAXResponse;
import app.website.api.facility.ListRoomAJAXRequest;
import app.website.api.facility.ListRoomAJAXResponse;
import app.website.api.facility.RoomAJAXView;
import app.website.api.facility.RoomAJAXWebService;
import core.framework.inject.Inject;
import java.util.stream.Collectors;

public class CompanyAJAXWebServiceImpl implements CompanyAJAXWebService {
    @Inject
    CompanyWebService companyWebService;

    @Override
    public ListCompanyAJAXResponse list() {
        ListCompanyResponse remoteResp = companyWebService.list();
        ListCompanyAJAXResponse resp = new ListCompanyAJAXResponse();
        resp.items = remoteResp.items.stream().map(c -> {
            CompanyAJAXView view = new CompanyAJAXView();
            view.id = c.id;
            view.name = c.name;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}