package app.backoffice.api.user;

import core.framework.api.json.Property;

public enum UserStatusAJAXView {
    @Property(name = "ACTIVE")
    ACTIVE,
    @Property(name = "INACTIVE")
    INACTIVE,
    @Property(name = "PENDING")
    PENDING
}