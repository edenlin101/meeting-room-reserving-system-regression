package app.backoffice.api.company;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListCompanyAJAXResponse {
    @NotNull
    @Property(name = "companies")
    public List<CompanyAJAXView> companies;
}