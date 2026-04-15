package app;

import app.reservation.ReservationModule;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class BookingApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new ReservationModule());
    }
}