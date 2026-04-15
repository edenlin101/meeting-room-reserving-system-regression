package app;

import app.facility.api.company.CompanyWebService;
import app.facility.api.room.RoomWebService;
import app.reservation.api.reservation.ReservationWebService;
import app.user.api.user.UserWebService;
import app.website.api.facility.CompanyAJAXWebService;
import app.website.api.facility.RoomAJAXWebService;
import app.website.api.reservation.ReservationAJAXWebService;
import app.website.api.user.UserAJAXWebService;
import app.website.interceptor.LoginInterceptor;
import app.website.web.facility.CompanyAJAXWebServiceImpl;
import app.website.web.facility.RoomAJAXWebServiceImpl;
import app.website.web.reservation.ReservationAJAXWebServiceImpl;
import app.website.web.user.UserAJAXWebServiceImpl;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.http.HTTPMethod;

public class WebsiteApp extends App {
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

        LoginInterceptor loginInterceptor = new LoginInterceptor();
        http().intercept(loginInterceptor);
    }
}