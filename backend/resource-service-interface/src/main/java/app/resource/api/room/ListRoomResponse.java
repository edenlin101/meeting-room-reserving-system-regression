package app.resource.api.room;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListRoomResponse {
    @NotNull
    @Property(name = "rooms")
    public List<RoomView> rooms;
}