package app;

import app.notification.NotificationModule;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class NotificationApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new NotificationModule());
    }
}