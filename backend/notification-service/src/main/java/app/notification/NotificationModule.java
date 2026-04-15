package app.notification;

import app.notification.handler.NotifyUpcomingReservationHandler;
import app.reservation.api.message.NotifyUpcomingReservationMessage;
import core.framework.module.Module;

public class NotificationModule extends Module {
    @Override
    protected void initialize() {
        kafka().subscribe("notify-upcoming-reservation-message", NotifyUpcomingReservationMessage.class, bind(NotifyUpcomingReservationHandler.class));
    }
}