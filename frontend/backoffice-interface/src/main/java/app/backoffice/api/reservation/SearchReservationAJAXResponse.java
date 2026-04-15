package app.backoffice.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class SearchReservationAJAXResponse {
    @NotNull
    @Property(name = "items")
    public List<ReservationAJAXView> items;
}