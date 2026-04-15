package app.backoffice.api.user;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class UpdateUserStatusAJAXRequest {
    @NotNull
    @Property(name = "user_id")
    public Long userId;

    @NotNull
    @Property(name = "status")
    public UserStatusAJAXView status;
}