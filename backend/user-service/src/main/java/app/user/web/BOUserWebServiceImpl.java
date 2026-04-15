package app.user.web;

import app.user.api.user.BOLoginUserRequest;
import app.user.api.user.BOLoginUserResponse;
import app.user.api.user.BOUpdateUserStatusRequest;
import app.user.api.user.BOUserWebService;
import app.user.api.user.LoginUserRequest;
import app.user.api.user.LoginUserResponse;
import app.user.service.UserService;
import core.framework.inject.Inject;

public class BOUserWebServiceImpl implements BOUserWebService {
    @Inject
    UserService userService;

    @Override
    public BOLoginUserResponse login(BOLoginUserRequest request) {
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
    public void updateStatus(BOUpdateUserStatusRequest request) {
        userService.updateStatus(request.id, request.status);
    }
}
