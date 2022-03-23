package com.kiwni.app.user.network;

public class AppConstants
{
    public static final String WEBSOCKET_RESERVATION_EVENT = "reservation_success";
    public static final String WEBSOCKET_DRIVER_DATA_EVENT = "driver_data_updated";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int SPLASH_SCREEN_TIME_OUT = 2000;

    public static final String BASE_URL = "https://api.dev.kiwni.com/";

    public static final String IMAGE_PATH = "https://kiwni.com/car_images/";

    public static final String SOCKET_BASE_URL = "https://broadcast-service-7qkkocxieq-el.a.run.app/users";
}
