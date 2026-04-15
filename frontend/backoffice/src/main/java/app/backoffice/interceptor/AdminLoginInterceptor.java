package app.backoffice.interceptor;

import core.framework.web.Interceptor;
import core.framework.web.Invocation;
import core.framework.web.Response;
import core.framework.web.exception.UnauthorizedException;
import core.framework.web.Session;

public class AdminLoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Invocation invocation) throws Exception {
        Session session = invocation.context().request().session();
        
        String adminIdStr = session.get("adminId").orElse(null);
        if (adminIdStr == null) {
            throw new UnauthorizedException("admin not logged in");
        }

        return invocation.proceed();
    }
}