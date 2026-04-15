package app.resource.api.company;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface CompanyWebService {
    @GET
    @Path("/company/list")
    ListCompanyResponse list();
}
