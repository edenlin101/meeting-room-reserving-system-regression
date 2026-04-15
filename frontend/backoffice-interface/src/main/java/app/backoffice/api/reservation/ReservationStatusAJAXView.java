package app.backoffice.api.reservation;

import core.framework.api.json.Property;

public enum ReservationStatusAJAXView {
    @Property(name = "RESERVED")
    RESERVED,
    @Property(name = "CANCELED")
    CANCELED
}