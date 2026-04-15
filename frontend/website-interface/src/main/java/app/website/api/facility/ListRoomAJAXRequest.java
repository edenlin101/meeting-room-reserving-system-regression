package app.website.api.facility;

import core.framework.api.web.service.QueryParam;

public class ListRoomAJAXRequest {
    @QueryParam(name = "company_id")
    public Long companyId;
}