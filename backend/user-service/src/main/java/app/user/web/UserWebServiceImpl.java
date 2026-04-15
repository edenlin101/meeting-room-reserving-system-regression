package app.user.web;

import app.user.api.user.LoginUserRequest;
import app.user.api.user.LoginUserResponse;
import app.user.api.user.RegisterUserRequest;
import app.user.api.user.RegisterUserResponse;
import app.user.api.user.UserWebService;
import app.user.service.UserService;
import core.framework.inject.Inject;

public class UserWebServiceImpl implements UserWebService {
    @Inject
    UserService userService;

    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {
        return userService.register(request);
    }

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        return userService.login(request);
    }
}
