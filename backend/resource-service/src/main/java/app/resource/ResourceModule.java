package app.resource;

import app.resource.api.company.BOCompanyWebService;
import app.resource.api.company.CompanyWebService;
import app.resource.api.room.BORoomWebService;
import app.resource.api.room.RoomWebService;
import app.resource.domain.Company;
import app.resource.domain.Room;
import app.resource.service.CompanyService;
import app.resource.service.RoomService;
import app.resource.web.BOCompanyWebServiceImpl;
import app.resource.web.CompanyWebServiceImpl;
import app.resource.web.BORoomWebServiceImpl;
import app.resource.web.RoomWebServiceImpl;
import core.framework.module.Module;

public class ResourceModule extends Module {
    @Override
    protected void initialize() {
        db().repository(Company.class);
        db().repository(Room.class);

        bind(CompanyService.class);
        bind(RoomService.class);

        api().service(CompanyWebService.class, bind(CompanyWebServiceImpl.class));
        api().service(BOCompanyWebService.class, bind(BOCompanyWebServiceImpl.class));
        api().service(RoomWebService.class, bind(RoomWebServiceImpl.class));
        api().service(BORoomWebService.class, bind(BORoomWebServiceImpl.class));
    }
}
