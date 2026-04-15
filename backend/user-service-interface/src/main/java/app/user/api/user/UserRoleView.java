package app.user.api.user;

import core.framework.api.json.Property;

public enum UserRoleView {
    @Property(name = "USER")
    USER,
    @Property(name = "ADMIN")
    ADMIN
}