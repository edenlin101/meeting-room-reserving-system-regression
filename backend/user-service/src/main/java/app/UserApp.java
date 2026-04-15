package app;

import app.user.UserModule;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class UserApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new UserModule());
    }
}