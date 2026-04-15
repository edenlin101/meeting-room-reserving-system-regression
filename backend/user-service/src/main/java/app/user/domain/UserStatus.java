package app.user.domain;

import core.framework.db.DBEnumValue;

public enum UserStatus {
    @DBEnumValue("ACTIVE")
    ACTIVE,
    @DBEnumValue("INACTIVE")
    INACTIVE,
    @DBEnumValue("PENDING")
    PENDING
}