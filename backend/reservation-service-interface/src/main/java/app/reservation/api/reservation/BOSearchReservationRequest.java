package app.reservation.api.reservation;

import core.framework.api.web.service.QueryParam;

import java.time.LocalDate;

public class BOSearchReservationRequest {
    @QueryParam(name = "company_id")
    public Long companyId;

    @QueryParam(name = "room_id")
    public Long roomId;

    @QueryParam(name = "date")
    public LocalDate date;
}