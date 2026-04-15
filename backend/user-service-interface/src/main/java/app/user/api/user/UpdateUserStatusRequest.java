package app.user.api.user;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class UpdateUserStatusRequest {
    @NotNull
    @Property(name = "id")
    public Long id;

    @NotNull
    @Property(name = "status")
    public UserStatusView status;
}