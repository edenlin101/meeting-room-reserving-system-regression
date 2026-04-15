package app.facility.api.company;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class CreateCompanyRequest {
    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;
}