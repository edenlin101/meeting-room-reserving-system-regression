package app.reservation.api.reservation;

import core.framework.api.json.Property;

public enum ReservationStatusView {
    @Property(name = "RESERVED")
    RESERVED,
    @Property(name = "CANCELED")
    CANCELED
}