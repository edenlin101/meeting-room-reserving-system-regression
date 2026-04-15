package app.reservation.api.message;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class NotifyUpcomingReservationMessage {
    @NotNull
    @Property(name = "reservation_id")
    public Long reservationId;

    @NotNull
    @Property(name = "user_id")
    public Long userId;

    @NotNull
    @Property(name = "room_id")
    public Long roomId;

    @NotNull
    @Property(name = "date")
    public LocalDate date;

    @NotNull
    @Property(name = "start_time")
    public LocalTime startTime;
}