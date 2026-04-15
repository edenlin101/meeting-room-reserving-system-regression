package app.reservation.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateReservationRequest {
    @NotNull
    @Property(name = "user_id")
    public Long userId;

    @NotNull
    @Property(name = "company_id")
    public Long companyId;

    @NotNull
    @Property(name = "room_id")
    public Long roomId;

    @NotNull
    @Property(name = "date")
    public LocalDate date;

    @NotNull
    @Property(name = "start_time")
    public LocalTime startTime;

    @NotNull
    @Property(name = "end_time")
    public LocalTime endTime;
}