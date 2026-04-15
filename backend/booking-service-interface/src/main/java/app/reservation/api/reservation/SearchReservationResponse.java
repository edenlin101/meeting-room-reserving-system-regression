package app.reservation.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class SearchReservationResponse {
    @NotNull
    @Property(name = "items")
    public List<ReservationView> items;
}