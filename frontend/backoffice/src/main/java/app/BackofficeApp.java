package app;

import app.resource.api.company.CompanyWebService;
import app.resource.api.company.BOCompanyWebService;
import app.resource.api.room.RoomWebService;
import app.resource.api.room.BORoomWebService;
import app.reservation.api.reservation.ReservationWebService;
import app.reservation.api.reservation.BOReservationWebService;
import app.user.api.user.UserWebService;
import app.user.api.user.BOUserWebService;
import app.backoffice.api.company.CompanyAJAXWebService;
import app.backoffice.api.room.RoomAJAXWebService;
import app.backoffice.api.reservation.ReservationAJAXWebService;
import app.backoffice.api.user.UserAJAXWebService;
import app.backoffice.interceptor.AdminLoginInterceptor;
import app.backoffice.web.facility.CompanyAJAXWebServiceImpl;
import app.backoffice.web.facility.RoomAJAXWebServiceImpl;
import app.backoffice.web.reservation.ReservationAJAXWebServiceImpl;
import app.backoffice.web.user.UserAJAXWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;

public class BackofficeApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));

        site().session().local();

        api().client(BOUserWebService.class, requiredProperty("app.user.url"));
        api().client(BOCompanyWebService.class, requiredProperty("app.resource.url"));
        api().client(BORoomWebService.class, requiredProperty("app.resource.url"));
        api().client(BOReservationWebService.class, requiredProperty("app.reservation.url"));

        api().service(UserAJAXWebService.class, bind(UserAJAXWebServiceImpl.class));
        api().service(CompanyAJAXWebService.class, bind(CompanyAJAXWebServiceImpl.class));
        api().service(RoomAJAXWebService.class, bind(RoomAJAXWebServiceImpl.class));
        api().service(ReservationAJAXWebService.class, bind(ReservationAJAXWebServiceImpl.class));

        AdminLoginInterceptor loginInterceptor = new AdminLoginInterceptor();
        http().intercept(loginInterceptor);
    }
}
