package app.user.api.user;

import core.framework.api.json.Property;

public enum UserStatusView {
    @Property(name = "ACTIVE")
    ACTIVE,
    @Property(name = "INACTIVE")
    INACTIVE,
    @Property(name = "PENDING")
    PENDING
}