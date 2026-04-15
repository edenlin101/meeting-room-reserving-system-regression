package app.backoffice.api.room;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

public class CreateRoomAJAXRequest {
    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;

    @NotNull
    @Property(name = "company_id")
    public Long companyId;
}