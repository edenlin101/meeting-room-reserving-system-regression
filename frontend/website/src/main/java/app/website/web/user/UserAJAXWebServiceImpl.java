package app.website.web.user;

import app.user.api.user.LoginUserRequest;
import app.user.api.user.LoginUserResponse;
import app.user.api.user.RegisterUserRequest;
import app.user.api.user.RegisterUserResponse;
import app.user.api.user.UserWebService;
import app.website.api.user.LoginUserAJAXRequest;
import app.website.api.user.LoginUserAJAXResponse;
import app.website.api.user.RegisterUserAJAXRequest;
import app.website.api.user.RegisterUserAJAXResponse;
import app.website.api.user.UserAJAXWebService;
import core.framework.inject.Inject;
import core.framework.web.Request;

public class UserAJAXWebServiceImpl implements UserAJAXWebService {
    @Inject
    UserWebService userWebService;

    @Inject
    Request request;

    @Override
    public RegisterUserAJAXResponse register(RegisterUserAJAXRequest req) {
        RegisterUserRequest remoteReq = new RegisterUserRequest();
        remoteReq.username = req.username;
        remoteReq.password = req.password;
        remoteReq.companyId = req.companyId;

        RegisterUserResponse remoteResp = userWebService.register(remoteReq);

        RegisterUserAJAXResponse resp = new RegisterUserAJAXResponse();
        resp.id = remoteResp.id;
        resp.username = remoteResp.username;
        return resp;
    }

    @Override
    public LoginUserAJAXResponse login(LoginUserAJAXRequest req) {
        LoginUserRequest remoteReq = new LoginUserRequest();
        remoteReq.username = req.username;
        remoteReq.password = req.password;

        LoginUserResponse remoteResp = userWebService.login(remoteReq);

        // set session
        request.session().set("userId", String.valueOf(remoteResp.id));
        request.session().set("companyId", String.valueOf(remoteResp.companyId));

        LoginUserAJAXResponse resp = new LoginUserAJAXResponse();
        resp.id = remoteResp.id;
        resp.username = remoteResp.username;
        resp.companyId = remoteResp.companyId;
        return resp;
    }
}