package app.reservation.domain;

import core.framework.db.DBEnumValue;

public enum ReservationStatus {
    @DBEnumValue("RESERVED")
    RESERVED,
    @DBEnumValue("CANCELED")
    CANCELED
}