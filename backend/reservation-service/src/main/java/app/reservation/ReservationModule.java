package app.reservation;

import app.reservation.api.message.CheckUpcomingReservationMessage;
import app.reservation.api.message.NotifyUpcomingReservationMessage;
import app.reservation.api.reservation.BOReservationWebService;
import app.reservation.api.reservation.ReservationWebService;
import app.reservation.domain.Reservation;
import app.reservation.domain.ReservationTracking;
import app.reservation.handler.CheckUpcomingReservationHandler;
import app.reservation.service.ReservationService;
import app.reservation.web.BOReservationWebServiceImpl;
import app.reservation.web.ReservationWebServiceImpl;
import core.framework.module.Module;
import core.framework.mongo.module.MongoConfig;

public class ReservationModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Reservation.class);
        
        config(MongoConfig.class).collection(ReservationTracking.class);

        bind(ReservationService.class);

        api().service(ReservationWebService.class, bind(ReservationWebServiceImpl.class));
        api().service(BOReservationWebService.class, bind(BOReservationWebServiceImpl.class));

        kafka().subscribe("check-upcoming-reservation-message", CheckUpcomingReservationMessage.class, bind(CheckUpcomingReservationHandler.class));
        kafka().publish("notify-upcoming-reservation-message", NotifyUpcomingReservationMessage.class);
    }
}
