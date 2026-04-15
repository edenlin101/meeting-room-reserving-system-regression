package app.facility.web;

import app.facility.api.company.CompanyWebService;
import app.facility.api.company.CreateCompanyRequest;
import app.facility.api.company.CreateCompanyResponse;
import app.facility.api.company.ListCompanyResponse;
import app.facility.service.CompanyService;
import core.framework.inject.Inject;

public class CompanyWebServiceImpl implements CompanyWebService {
    @Inject
    CompanyService companyService;

    @Override
    public CreateCompanyResponse create(CreateCompanyRequest request) {
        return companyService.create(request);
    }

    @Override
    public void delete(Long id) {
        companyService.delete(id);
    }

    @Override
    public ListCompanyResponse list() {
        return companyService.list();
    }
}