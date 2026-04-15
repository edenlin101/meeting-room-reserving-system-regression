package app;

import core.framework.db.DBConfig;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class BookingServiceDBMigrationApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        DBConfig db = db();
        db.url(requiredProperty("sys.jdbc.url"));
        db.user(requiredProperty("sys.jdbc.user"));
        db.password(requiredProperty("sys.jdbc.password"));
    }
}