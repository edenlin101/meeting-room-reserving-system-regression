package app.website.api.room;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListRoomAJAXResponse {
    @NotNull
    @Property(name = "rooms")
    public List<RoomAJAXView> rooms;
}