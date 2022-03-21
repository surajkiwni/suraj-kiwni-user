package com.kiwni.app.user.sharedpref;

public class SharedPref
{
    /*private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    Context context;*/

    public static final String SHARED_PREF_NAME = "userPref";

    //Otp Screen
    public static final String FIREBASE_UID = "uid";
    public static final String FIREBASE_EMAIL = "email";
    public static final String FIREBASE_MOBILE_NO = "mobile_no";
    public static final String FIREBASE_USERNAME = "username";
    public static final String FIREBASE_TOKEN = "idToken";
    public static final String FIREBASE_REFRESH_TOKEN = "refreshToken";
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
    public static final String DISTANCE_IN_KM = "distanceInKm";
    public static final String PICKUP_TIME_FOR_API = "pickupTimeForApi";
    public static final String DROP_TIME_FOR_API = "dropTimeForApi";
    public static final String DURATION_IN_TRAFFIC = "durationInTraffic";
    public static final String PICKUP_DATE_TO_DISPLAY = "pickupDateToDisplay";
    public static final String DROP_DATE_TO_DISPLAY = "dropDateToDisplay";
    public static final String PICKUP_TIME_TO_DISPLAY = "pickupTimeToDisplay";

    //Find car screen
    public static final String VEHICLE_ID = "vehicleId";
    public static final String VEHICLE_BRAND = "vehicleBrand";
    public static final String VEHICLE_MODEL = "vehicleModel";
    public static final String VEHICLE_MODEL_IMAGE = "vehicleModelImage";
    public static final String VEHICLE_CLASS_TYPE = "vehicleClassType";
    public static final String VEHICLE_CAPACITY = "vehicleCapacity";
    public static final String VEHICLE_START_TIME = "vehicleStartTime";
    public static final String VEHICLE_NO = "vehicleNo";
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

    public static final String RESERVATION_SUCCESS = "reservation_success";

    /*public SharedPref(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public void setUserData(String data)
    {

    }*/
}
