package app.resource.api.company;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class BOListCompanyResponse {
    @NotNull
    @Property(name = "companies")
    public List<CompanyView> companies;
}