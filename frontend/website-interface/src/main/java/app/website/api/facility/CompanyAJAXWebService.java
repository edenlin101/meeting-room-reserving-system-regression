package app.website.api.facility;

import core.framework.api.web.service.GET;
import core.framework.api.web.service.Path;

public interface CompanyAJAXWebService {
    @GET
    @Path("/ajax/company/list")
    ListCompanyAJAXResponse list();
}