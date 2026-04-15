package app;

import app.facility.api.company.CompanyWebService;
import app.facility.api.room.RoomWebService;
import app.reservation.api.reservation.ReservationWebService;
import app.user.api.user.UserWebService;
import app.backoffice.api.facility.CompanyAJAXWebService;
import app.backoffice.api.facility.RoomAJAXWebService;
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

        api().client(UserWebService.class, requiredProperty("app.user.url"));
        api().client(CompanyWebService.class, requiredProperty("app.resource.url"));
        api().client(RoomWebService.class, requiredProperty("app.resource.url"));
        api().client(ReservationWebService.class, requiredProperty("app.booking.url"));

        api().service(UserAJAXWebService.class, bind(UserAJAXWebServiceImpl.class));
        api().service(CompanyAJAXWebService.class, bind(CompanyAJAXWebServiceImpl.class));
        api().service(RoomAJAXWebService.class, bind(RoomAJAXWebServiceImpl.class));
        api().service(ReservationAJAXWebService.class, bind(ReservationAJAXWebServiceImpl.class));

        AdminLoginInterceptor loginInterceptor = new AdminLoginInterceptor();
        http().intercept(loginInterceptor);
    }
}