package app.backoffice.api.facility;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface CompanyAJAXWebService {
    @POST
    @Path("/ajax/company")
    @ResponseStatus(HTTPStatus.CREATED)
    CreateCompanyAJAXResponse create(CreateCompanyAJAXRequest request);

    @DELETE
    @Path("/ajax/company/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/ajax/company/list")
    ListCompanyAJAXResponse list();
}