package app.reservation.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class SearchAvailabilityResponse {
    @NotNull
    @Property(name = "time_slots")
    public List<TimeSlotView> timeSlots;
}