package app.facility.api.room;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

public class ListRoomResponse {
    @NotNull
    @Property(name = "items")
    public List<RoomView> items;
}