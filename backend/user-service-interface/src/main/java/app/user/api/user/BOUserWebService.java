package app.user.api.user;

import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;

public interface BOUserWebService {
    @POST
    @Path("/bo/user/admin-login")
    BOLoginUserResponse login(BOLoginUserRequest request);

    @PUT
    @Path("/bo/user/admin-status")
    void updateStatus(BOUpdateUserStatusRequest request);
}
