package app.resource.api.company;

import core.framework.api.web.service.DELETE;
import core.framework.api.web.service.GET;
import core.framework.api.web.service.POST;
import core.framework.api.web.service.Path;
import core.framework.api.web.service.PathParam;
import core.framework.api.web.service.ResponseStatus;
import core.framework.api.http.HTTPStatus;

public interface BOCompanyWebService {
    @POST
    @Path("/bo/company")
    @ResponseStatus(HTTPStatus.CREATED)
    BOCreateCompanyResponse create(BOCreateCompanyRequest request);

    @DELETE
    @Path("/bo/company/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") Long id);

    @GET
    @Path("/bo/company/list")
    BOListCompanyResponse list();
}
