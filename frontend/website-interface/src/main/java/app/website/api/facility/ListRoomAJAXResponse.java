package app.website.api.facility;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListRoomAJAXResponse {
    @NotNull
    @Property(name = "items")
    public List<RoomAJAXView> items;
}