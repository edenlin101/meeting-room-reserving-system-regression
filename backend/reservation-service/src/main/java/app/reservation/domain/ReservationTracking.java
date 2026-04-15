package app.reservation.domain;

import core.framework.mongo.Collection;
import core.framework.mongo.Field;
import core.framework.mongo.Id;
import core.framework.mongo.MongoEnumValue;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;

@Collection(name = "reservation_trackings")
public class ReservationTracking {
    @Id
    public ObjectId id;

    @Field(name = "reservation_id")
    public Long reservationId;

    @Field(name = "action")
    public TrackingAction action;

    @Field(name = "user_id")
    public Long userId;

    @Field(name = "timestamp")
    public ZonedDateTime timestamp;

    @Field(name = "details")
    public String details;
}