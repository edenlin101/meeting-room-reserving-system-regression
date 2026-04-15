package app.backoffice.api.user;

import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;

public interface UserAJAXWebService {
    @POST
    @Path("/ajax/admin/login")
    AdminLoginAJAXResponse login(AdminLoginAJAXRequest request);

    @PUT
    @Path("/ajax/user/status")
    void updateStatus(UpdateUserStatusAJAXRequest request);
}