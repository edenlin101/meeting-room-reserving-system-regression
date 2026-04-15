package app.facility.api.room;

import core.framework.api.web.service.QueryParam;

public class ListRoomRequest {
    @QueryParam(name = "company_id")
    public Long companyId;
}