package com.kiwni.app.user.network;

import com.kiwni.app.user.models.ScheduleDates;
import com.kiwni.app.user.models.ScheduleMapResp;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface
{
    String GOOGLE_MAP_API_KEY = "AIzaSyCC3Tk24iI-IU8SvXkzDUj2i2mBeFjw3-s";
    String FIREBASE_API_KEY = "AIzaSyCPVjcxB3zcRWeT35k3876rhTL0lyUWFS0";

    @POST("/projection/api/schedules/map/")
    Call<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>>
    getProjectionScheduleDates(@Body ScheduleDates scheduleDatesModel,
                               @Header("Authorization") String authHeader);

    /*@POST("/core/api/persons/")
    Call<UserRegisterResponse>
    createUser(@Body UserRegister userModel);

    @POST("/projection/api/schedules/map/")
    Call<Map<String, List<ScheduleDatesResp>>>
    getProjectionScheduleDates(@Body ScheduleDates scheduleDatesModel,
                               @Header("Authorization") String authHeader);*/


    /*@POST("/projection/api/schedules/map/")
    Call<JsonElement>
    getProjectionScheduleDates(@Body JsonObject scheduleDatesModel,
                               @Header("Authorization") String authHeader);*/

    /*@POST("/reservation/api/reservations/")
    Call<RideReservationResp>
    createRide(@Body RideReservationReq reservationReq,
               @Header("Authorization") String header);

    @GET("/trip/api/trips/user/{id}/")
    Call<List<TripsHistoryResp>> getTripHistory(@Path("id") int id);*/
}
