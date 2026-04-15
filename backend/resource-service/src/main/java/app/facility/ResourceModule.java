package app.facility;

import app.facility.api.company.CompanyWebService;
import app.facility.api.room.RoomWebService;
import app.facility.domain.Company;
import app.facility.domain.Room;
import app.facility.service.CompanyService;
import app.facility.service.RoomService;
import app.facility.web.CompanyWebServiceImpl;
import app.facility.web.RoomWebServiceImpl;
import core.framework.module.Module;

public class ResourceModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Company.class);
        db().repository(Room.class);

        bind(CompanyService.class);
        bind(RoomService.class);

        api().service(CompanyWebService.class, bind(CompanyWebServiceImpl.class));
        api().service(RoomWebService.class, bind(RoomWebServiceImpl.class));
    }
}