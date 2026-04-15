package app.backoffice.web.user;

import app.user.api.user.BOLoginUserRequest;
import app.user.api.user.BOLoginUserResponse;
import app.user.api.user.BOUpdateUserStatusRequest;
import app.user.api.user.UserStatusView;
import app.user.api.user.BOUserWebService;
import app.backoffice.api.user.AdminLoginAJAXRequest;
import app.backoffice.api.user.AdminLoginAJAXResponse;
import app.backoffice.api.user.UpdateUserStatusAJAXRequest;
import app.backoffice.api.user.UserAJAXWebService;
import core.framework.inject.Inject;
import core.framework.web.Request;

public class UserAJAXWebServiceImpl implements UserAJAXWebService {
    @Inject
    BOUserWebService userWebService;

    @Inject
    Request request;

    @Override
    public AdminLoginAJAXResponse login(AdminLoginAJAXRequest req) {
        BOLoginUserRequest remoteReq = new BOLoginUserRequest();
        remoteReq.username = req.username;
        remoteReq.password = req.password;

        BOLoginUserResponse remoteResp = userWebService.login(remoteReq);

        request.session().set("adminId", String.valueOf(remoteResp.id));

        AdminLoginAJAXResponse resp = new AdminLoginAJAXResponse();
        resp.id = remoteResp.id;
        resp.username = remoteResp.username;
        return resp;
    }

    @Override
    public void updateStatus(UpdateUserStatusAJAXRequest req) {
        BOUpdateUserStatusRequest remoteReq = new BOUpdateUserStatusRequest();
        remoteReq.id = req.userId;
        remoteReq.status = UserStatusView.valueOf(req.status.name());
        
        userWebService.updateStatus(remoteReq);
    }
}
