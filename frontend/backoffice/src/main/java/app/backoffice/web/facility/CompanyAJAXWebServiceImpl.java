package app.backoffice.web.facility;

import app.facility.api.company.CompanyWebService;
import app.facility.api.company.CreateCompanyRequest;
import app.facility.api.company.CreateCompanyResponse;
import app.facility.api.company.ListCompanyResponse;
import app.backoffice.api.facility.CompanyAJAXView;
import app.backoffice.api.facility.CompanyAJAXWebService;
import app.backoffice.api.facility.CreateCompanyAJAXRequest;
import app.backoffice.api.facility.CreateCompanyAJAXResponse;
import app.backoffice.api.facility.ListCompanyAJAXResponse;
import core.framework.inject.Inject;

import java.util.stream.Collectors;

public class CompanyAJAXWebServiceImpl implements CompanyAJAXWebService {
    @Inject
    CompanyWebService companyWebService;

    @Override
    public CreateCompanyAJAXResponse create(CreateCompanyAJAXRequest req) {
        CreateCompanyRequest remoteReq = new CreateCompanyRequest();
        remoteReq.name = req.name;

        CreateCompanyResponse remoteResp = companyWebService.create(remoteReq);

        CreateCompanyAJAXResponse resp = new CreateCompanyAJAXResponse();
        resp.id = remoteResp.id;
        resp.name = remoteResp.name;
        return resp;
    }

    @Override
    public void delete(Long id) {
        companyWebService.delete(id);
    }

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