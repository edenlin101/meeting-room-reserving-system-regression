package app.website.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;
import core.framework.api.web.service.QueryParam;

import java.time.LocalDate;

public class SearchAvailabilityAJAXRequest {
    @NotNull
    @QueryParam(name = "room_id")
    public Long roomId;

    @NotNull
    @QueryParam(name = "date")
    public LocalDate date;
}