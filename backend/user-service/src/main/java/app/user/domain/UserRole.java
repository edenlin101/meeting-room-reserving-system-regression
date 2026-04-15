package app.user.domain;

import core.framework.db.DBEnumValue;

public enum UserRole {
    @DBEnumValue("USER")
    USER,
    @DBEnumValue("ADMIN")
    ADMIN
}