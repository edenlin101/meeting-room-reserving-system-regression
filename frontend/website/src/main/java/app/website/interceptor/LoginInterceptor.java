package app.website.interceptor;

import core.framework.api.http.HTTPStatus;
import core.framework.web.Interceptor;
import core.framework.web.Invocation;
import core.framework.web.Response;
import core.framework.web.exception.UnauthorizedException;
import core.framework.web.Session;

public class LoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Invocation invocation) throws Exception {
        Session session = invocation.context().request().session();
        
        String userIdStr = session.get("userId").orElse(null);
        if (userIdStr == null) {
            throw new UnauthorizedException("user not logged in");
        }

        return invocation.proceed();
    }
}