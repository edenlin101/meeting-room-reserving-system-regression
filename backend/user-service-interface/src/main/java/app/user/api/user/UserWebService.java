package app.user.api.user;

import core.framework.api.web.service.POST;
import core.framework.api.web.service.PUT;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface UserWebService {
    @POST
    @Path("/user/register")
    @ResponseStatus(HTTPStatus.CREATED)
    RegisterUserResponse register(RegisterUserRequest request);

    @POST
    @Path("/user/login")
    LoginUserResponse login(LoginUserRequest request);
}
