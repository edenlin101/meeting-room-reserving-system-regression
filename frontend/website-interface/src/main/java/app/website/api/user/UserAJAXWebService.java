package app.website.api.user;

import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface UserAJAXWebService {
    @POST
    @Path("/ajax/user/register")
    @ResponseStatus(HTTPStatus.CREATED)
    RegisterUserAJAXResponse register(RegisterUserAJAXRequest request);

    @POST
    @Path("/ajax/user/login")
    LoginUserAJAXResponse login(LoginUserAJAXRequest request);
}