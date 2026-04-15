package app;

import app.facility.ResourceModule;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class ResourceApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new ResourceModule());
    }
}