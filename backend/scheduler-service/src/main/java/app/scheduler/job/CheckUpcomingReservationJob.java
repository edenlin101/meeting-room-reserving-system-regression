package app.scheduler.job;

import app.reservation.api.message.CheckUpcomingReservationMessage;
import core.framework.inject.Inject;
import core.framework.kafka.MessagePublisher;
import core.framework.scheduler.Job;
import core.framework.scheduler.JobContext;

import java.util.UUID;

public class CheckUpcomingReservationJob implements Job {
    @Inject
    MessagePublisher<CheckUpcomingReservationMessage> publisher;

    @Override
    public void execute(JobContext context) throws Exception {
        CheckUpcomingReservationMessage message = new CheckUpcomingReservationMessage();
        message.triggerId = UUID.randomUUID().toString();
        publisher.publish("trigger-" + message.triggerId, message);
    }
}