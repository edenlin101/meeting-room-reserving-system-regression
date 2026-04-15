package app.resource.api.company;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.api.validate.NotBlank;

public class CreateCompanyResponse {
    @NotNull
    @Property(name = "id")
    public Long id;

    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;
}