package app.reservation.handler;

import app.reservation.api.message.CheckUpcomingReservationMessage;
import app.reservation.api.message.NotifyUpcomingReservationMessage;
import app.reservation.domain.Reservation;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.kafka.MessageHandler;
import core.framework.kafka.MessagePublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class CheckUpcomingReservationHandler implements MessageHandler<CheckUpcomingReservationMessage> {
    @Inject
    Repository<Reservation> reservationRepository;

    @Inject
    MessagePublisher<NotifyUpcomingReservationMessage> publisher;

    @Override
    public void handle(String key, CheckUpcomingReservationMessage message) throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")); // Using a fixed zone for logic
        LocalDate today = now.toLocalDate();
        LocalTime timeToCheck = now.toLocalTime().plusMinutes(10);

        List<Reservation> upcoming = reservationRepository.select("date = ? AND start_time >= ? AND start_time < ? AND status = 'RESERVED'",
            today, timeToCheck.minusMinutes(1), timeToCheck.plusMinutes(1));

        for (Reservation r : upcoming) {
            NotifyUpcomingReservationMessage notifyMsg = new NotifyUpcomingReservationMessage();
            notifyMsg.reservationId = r.id;
            notifyMsg.userId = r.userId;
            notifyMsg.roomId = r.roomId;
            notifyMsg.date = r.date;
            notifyMsg.startTime = r.startTime;

            publisher.publish(String.valueOf(r.id), notifyMsg);
        }
    }
}