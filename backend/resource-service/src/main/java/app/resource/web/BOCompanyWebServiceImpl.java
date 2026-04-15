package app.resource.web;

import app.resource.api.company.BOCompanyWebService;
import app.resource.api.company.BOCreateCompanyRequest;
import app.resource.api.company.BOCreateCompanyResponse;
import app.resource.api.company.BOListCompanyResponse;
import app.resource.api.company.CreateCompanyRequest;
import app.resource.api.company.CreateCompanyResponse;
import app.resource.api.company.ListCompanyResponse;
import app.resource.service.CompanyService;
import core.framework.inject.Inject;

public class BOCompanyWebServiceImpl implements BOCompanyWebService {
    @Inject
    CompanyService companyService;

    @Override
    public BOCreateCompanyResponse create(BOCreateCompanyRequest request) {
        CreateCompanyRequest req = new CreateCompanyRequest();
        req.name = request.name;
        
        CreateCompanyResponse resp = companyService.create(req);
        
        BOCreateCompanyResponse boResp = new BOCreateCompanyResponse();
        boResp.id = resp.id;
        boResp.name = resp.name;
        return boResp;
    }

    @Override
    public void delete(Long id) {
        companyService.delete(id);
    }

    @Override
    public BOListCompanyResponse list() {
        ListCompanyResponse resp = companyService.list();
        
        BOListCompanyResponse boResp = new BOListCompanyResponse();
        boResp.companies = resp.companies;
        return boResp;
    }
}
