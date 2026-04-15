package app.user.api.user;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class RegisterUserAJAXRequest {
    @NotNull
    @NotBlank
    @Property(name = "username")
    public String username;

    @NotNull
    @Property(name = "company_id")
    public Long companyId;
}
