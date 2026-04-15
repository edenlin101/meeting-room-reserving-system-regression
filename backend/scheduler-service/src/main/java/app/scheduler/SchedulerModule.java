package app.scheduler;

import app.reservation.api.message.CheckUpcomingReservationMessage;
import app.scheduler.job.CheckUpcomingReservationJob;
import core.framework.module.Module;

import java.time.Duration;

public class SchedulerModule extends Module {
    @Override
    protected void initialize() {
        kafka().publish("check-upcoming-reservation-message", CheckUpcomingReservationMessage.class);
        
        bind(CheckUpcomingReservationJob.class);
        
        schedule().fixedRate("check-upcoming-reservation", bind(CheckUpcomingReservationJob.class), Duration.ofMinutes(1));
    }
}