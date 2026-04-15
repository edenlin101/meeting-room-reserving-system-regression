package app.reservation.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class CreateReservationResponse {
    @NotNull
    @Property(name = "id")
    public Long id;
}