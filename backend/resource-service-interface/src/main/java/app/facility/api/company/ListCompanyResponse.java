package app.facility.api.company;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListCompanyResponse {
    @NotNull
    @Property(name = "items")
    public List<CompanyView> items;
}