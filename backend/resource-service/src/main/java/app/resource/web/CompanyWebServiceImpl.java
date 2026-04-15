package app.resource.web;

import app.resource.api.company.CompanyWebService;
import app.resource.api.company.ListCompanyResponse;
import app.resource.service.CompanyService;
import core.framework.inject.Inject;

public class CompanyWebServiceImpl implements CompanyWebService {
    @Inject
    CompanyService companyService;

    @Override
    public ListCompanyResponse list() {
        return companyService.list();
    }
}
