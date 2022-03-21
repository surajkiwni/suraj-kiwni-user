package com.kiwni.app.user.global;

/**
 * Created by Sourav Das on 20/12/2021.
 */

public class SharedPreferencesConstant {

    public static final String SHARED_PREF_NAME = "userPref";

    //Otp Screen
    public static final String FIREBASE_UID = "uid";
    public static final String FIREBASE_EMAIL = "email";
    public static final String FIREBASE_MOBILE_NO = "mobileno";
    public static final String FIREBASE_USERNAME = "username";
    public static final String FIREBASE_TOKEN = "idToken";
    public static final String hasLoggedIn = "hasLoggedIn";
    public static final String partyId = "partyId";

    //Home Screen
    public static final String USER_CURRENT_LAT = "currLocLatitude";
    public static final String USER_CURRENT_LNG = "currLocLongitude";
    public static final String SERVICE_TYPE = "serviceType";

    //Outstation Screen
    public static final String PICKUP_CITY = "pickupCity";
    public static final String DROP_CITY = "dropCity";
    public static final String PICKUP_ADDRESS = "pickupAddress";
    public static final String DROP_ADDRESS = "dropAddress";
    public static final String PICKUP_LOCATION = "pickupLocation";
    public static final String DROP_LOCATION = "dropLocation";
    public static final String PICKUP_DATE = "pickupDate";
    public static final String START_DATE_WITH_MONTH_DAY = "startDateWithMonthDay";
    public static final String DIRECTION = "direction";
    public static final String DISTANCE = "distance";
    public static final String PICKUP_TIME_FOR_API = "pickupTimeForApi";
    public static final String DROP_TIME_FOR_API = "dropTimeForApi";
    public static final String DURATION_IN_TRAFFIC = "durationInTraffic";

    //Find car screen
    public static final String VEHICLE_ID = "vehicleId";
    public static final String VEHICLE_BRAND = "vehicleBrand";
    public static final String VEHICLE_MODEL = "vehicleModel";
    public static final String VEHICLE_CLASS_TYPE = "vehicleClassType";
    public static final String VEHICLE_CAPACITY = "vehicleCapacity";
    public static final String VEHICLE_START_TIME = "vehicleStartTime";
    /*public static final String VEHICLE_AGE = "vehicleAge";
    public static final String VEHICLE_LUGGAGE = "vehicleLuggage";
    public static final String VEHICLE_AC = "vehicleAC";
    public static final String VEHICLE_RATING = "vehicleRating";*/
    public static final String VEHICLE_RATES = "rates";
    //public static final String VEHICLE_EXTRA_KM = "vehicleExtraKm";
    public static final String VEHICLE_PRICE = "vehiclePrice";
    public static final String DRIVER_ID = "driverId";
    public static final String DRIVER_NAME = "driverName";
    public static final String DRIVER_PHONE = "driverPhone";
    public static final String DRIVER_LICENSE = "driverLicense";

    //Send To api
    public static final String PROVIDER_ID = "providerId";
    public static final String PROVIDER_NAME = "providerName";
    public static final String SCHEDULED_ID = "scheduleId";

    public static final String PAID_METHOD = "paid_method";
}
