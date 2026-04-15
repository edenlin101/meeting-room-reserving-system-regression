package app.reservation.domain;

import core.framework.db.Column;
import core.framework.db.DBEnumValue;
import core.framework.db.PrimaryKey;
import core.framework.db.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Table(name = "reservations")
public class Reservation {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

    @Column(name = "user_id")
    public Long userId;

    @Column(name = "company_id")
    public Long companyId;

    @Column(name = "room_id")
    public Long roomId;

    @Column(name = "date")
    public LocalDate date;

    @Column(name = "start_time")
    public LocalTime startTime;

    @Column(name = "end_time")
    public LocalTime endTime;

    @Column(name = "status")
    public ReservationStatus status;

    @Column(name = "created_at")
    public ZonedDateTime createdAt;
}