package app.website.api.reservation;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

public class ReserveRoomAJAXResponse {
    @NotNull
    @Property(name = "id")
    public Long id;
}