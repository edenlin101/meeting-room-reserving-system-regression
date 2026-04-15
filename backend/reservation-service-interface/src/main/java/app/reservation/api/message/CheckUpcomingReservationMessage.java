package app.reservation.api.message;

import core.framework.api.json.Property;

public class CheckUpcomingReservationMessage {
    @Property(name = "trigger_id")
    public String triggerId;
}