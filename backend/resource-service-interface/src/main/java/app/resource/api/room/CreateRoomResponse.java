package app.resource.api.room;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.api.validate.NotBlank;

public class CreateRoomResponse {
    @NotNull
    @Property(name = "id")
    public Long id;

    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;

    @NotNull
    @Property(name = "company_id")
    public Long companyId;
}