package app.notification.handler;

import app.reservation.api.message.NotifyUpcomingReservationMessage;
import core.framework.kafka.MessageHandler;

public class NotifyUpcomingReservationHandler implements MessageHandler<NotifyUpcomingReservationMessage> {
    @Override
    public void handle(String key, NotifyUpcomingReservationMessage message) throws Exception {
        // Mock notification logic
        System.out.println("Sending notification to user " + message.userId + " for reservation " + message.reservationId);
    }
}