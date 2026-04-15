package app.user.web;

import app.user.api.user.BOLoginUserRequest;
import app.user.api.user.BOLoginUserResponse;
import app.user.api.user.BOUpdateUserStatusRequest;
import app.user.api.user.LoginUserRequest;
import app.user.api.user.LoginUserResponse;
import app.user.api.user.RegisterUserRequest;
import app.user.api.user.RegisterUserResponse;
import app.user.api.user.UpdateUserStatusRequest;
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

    @Override
    public BOLoginUserResponse boLogin(BOLoginUserRequest request) {
        LoginUserRequest req = new LoginUserRequest();
        req.username = request.username;
        req.password = request.password;
        LoginUserResponse resp = userService.login(req);
        
        BOLoginUserResponse boResp = new BOLoginUserResponse();
        boResp.id = resp.id;
        boResp.username = resp.username;
        return boResp;
    }

    @Override
    public void updateStatus(UpdateUserStatusRequest request) {
        userService.updateStatus(request.id, request.status);
    }

    @Override
    public void boUpdateStatus(BOUpdateUserStatusRequest request) {
        userService.updateStatus(request.id, request.status);
    }
}