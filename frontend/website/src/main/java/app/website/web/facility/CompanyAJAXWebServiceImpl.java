package app.website.web.facility;

import app.resource.api.company.CompanyWebService;
import app.resource.api.company.ListCompanyResponse;
import app.resource.api.room.ListRoomRequest;
import app.resource.api.room.ListRoomResponse;
import app.resource.api.room.RoomWebService;
import app.website.api.company.CompanyAJAXView;
import app.website.api.company.CompanyAJAXWebService;
import app.website.api.company.ListCompanyAJAXResponse;
import app.website.api.room.ListRoomAJAXRequest;
import app.website.api.room.ListRoomAJAXResponse;
import app.website.api.room.RoomAJAXView;
import app.website.api.room.RoomAJAXWebService;
import core.framework.inject.Inject;
import java.util.stream.Collectors;

public class CompanyAJAXWebServiceImpl implements CompanyAJAXWebService {
    @Inject
    CompanyWebService companyWebService;

    @Override
    public ListCompanyAJAXResponse list() {
        ListCompanyResponse remoteResp = companyWebService.list();
        ListCompanyAJAXResponse resp = new ListCompanyAJAXResponse();
        resp.companies = remoteResp.companies.stream().map(c -> {
            CompanyAJAXView view = new CompanyAJAXView();
            view.id = c.id;
            view.name = c.name;
            return view;
        }).collect(Collectors.toList());
        return resp;
    }
}