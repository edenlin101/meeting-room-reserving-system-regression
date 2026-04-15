package app.backoffice.api.facility;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class CreateCompanyAJAXRequest {
    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;
}