package app;

import app.scheduler.SchedulerModule;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class SchedulerApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new SchedulerModule());
    }
}