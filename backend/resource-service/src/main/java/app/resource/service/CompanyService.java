package app.resource.service;

import app.resource.api.company.CompanyView;
import app.resource.api.company.CreateCompanyRequest;
import app.resource.api.company.CreateCompanyResponse;
import app.resource.api.company.ListCompanyResponse;
import app.resource.domain.Company;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.web.exception.ConflictException;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyService {
    @Inject
    Repository<Company> companyRepository;

    public CreateCompanyResponse create(CreateCompanyRequest request) {
        if (!companyRepository.select("name = ?", request.name).isEmpty()) {
            throw new ConflictException("company already exists");
        }

        Company company = new Company();
        company.name = request.name;
        company.id = companyRepository.insert(company).orElseThrow();

        CreateCompanyResponse response = new CreateCompanyResponse();
        response.id = company.id;
        response.name = company.name;
        return response;
    }

    public void delete(Long id) {
        companyRepository.delete(id);
    }

    public ListCompanyResponse list() {
        List<Company> companies = companyRepository.select("1=1");
        ListCompanyResponse response = new ListCompanyResponse();
        response.companies = companies.stream().map(c -> {
            CompanyView view = new CompanyView();
            view.id = c.id;
            view.name = c.name;
            return view;
        }).collect(Collectors.toList());
        return response;
    }
}