package app.reservation.domain;

import core.framework.mongo.MongoEnumValue;

public enum TrackingAction {
    @MongoEnumValue("CREATE")
    CREATE,
    @MongoEnumValue("CANCEL")
    CANCEL
}