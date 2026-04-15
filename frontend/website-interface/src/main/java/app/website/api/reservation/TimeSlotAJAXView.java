package app.website.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.LocalTime;

public class TimeSlotAJAXView {
    @NotNull
    @Property(name = "start_time")
    public LocalTime startTime;

    @NotNull
    @Property(name = "end_time")
    public LocalTime endTime;

    @NotNull
    @Property(name = "is_available")
    public Boolean isAvailable;
}