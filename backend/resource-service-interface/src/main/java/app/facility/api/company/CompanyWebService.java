package app.facility.api.company;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface CompanyWebService {
    @POST
    @Path("/company")
    @ResponseStatus(HTTPStatus.CREATED)
    CreateCompanyResponse create(CreateCompanyRequest request);

    @DELETE
    @Path("/company/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/company/list")
    ListCompanyResponse list();
}