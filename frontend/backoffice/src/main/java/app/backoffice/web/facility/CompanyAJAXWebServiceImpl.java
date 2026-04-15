package app.backoffice.web.facility;

import app.resource.api.company.BOCompanyWebService;
import app.resource.api.company.BOCreateCompanyRequest;
import app.resource.api.company.BOCreateCompanyResponse;
import app.resource.api.company.BOListCompanyResponse;
import app.backoffice.api.company.CompanyAJAXView;
import app.backoffice.api.company.CompanyAJAXWebService;
import app.backoffice.api.company.CreateCompanyAJAXRequest;
import app.backoffice.api.company.CreateCompanyAJAXResponse;
import app.backoffice.api.company.ListCompanyAJAXResponse;
import core.framework.inject.Inject;
import java.util.stream.Collectors;

public class CompanyAJAXWebServiceImpl implements CompanyAJAXWebService {
    @Inject
    BOCompanyWebService companyWebService;

    @Override
    public CreateCompanyAJAXResponse create(CreateCompanyAJAXRequest req) {
        BOCreateCompanyRequest remoteReq = new BOCreateCompanyRequest();
        remoteReq.name = req.name;

        BOCreateCompanyResponse remoteResp = companyWebService.create(remoteReq);

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
        BOListCompanyResponse remoteResp = companyWebService.list();

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
