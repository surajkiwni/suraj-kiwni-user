package com.kiwni.app.user.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kiwni.app.user.global.NetworkConstant;
import com.kiwni.app.user.network.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sourav on 21/12/2021.
 */

public class ServiceConnector {
    static ServiceConnector ourInstance;
    final String TAG = this.getClass().getSimpleName();
    private String authorization = null;
    private Retrofit retrofit;

    public synchronized static ServiceConnector getInstance() {
        if (ourInstance == null) {
            ourInstance = new ServiceConnector();
        }
        return ourInstance;
    }

    private ServiceConnector()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        //client.conn

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(NetworkConstant.RESERVATION_BASE_URL)
                .build();
    }

    private MultipartBody.Part createMultipartBody(String name, File file, String type) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(name, file.getName(), reqFile);
    }

    private MultipartBody.Part createMultipartVideoBody(String name, File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
        return MultipartBody.Part.createFormData(name, file.getName(), reqFile);
    }

    /*private Call<JsonElement> logout() {
        return retrofit.create(AuthenticateService.class).logout(authorization);
    }*/

    public synchronized void addRequestToQueue(Call<JsonElement> request, final IServiceReceiver receiverCallback, final IServiceError errorCallback)
    {
        request.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (response.isSuccessful())
                {
                    receiverCallback.onReceiver(response.body());
                }
                else
                {
                    try {
                        String json = response.errorBody().string();
                        if (response.code() == 422) {
                            errorCallback.onError(response.code(), json);
                        } if (response.code() == 400) {
                            errorCallback.onError(response.code(), json);
                        } else {
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse(json).getAsJsonObject();
                            if (obj.has("message")) {
                                errorCallback.onError(response.code(), obj.get("message").getAsString());
                            } else if(obj.has("error")) {
                                errorCallback.onError(response.code(), obj.get("error").getAsString());
                            } else {
                                errorCallback.onError(response.code(), response.message());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorCallback.onError(response.code(), response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (t instanceof ConnectException) {
                    errorCallback.onError(-100, "Failed to connect to server, please check internet connection!");
                } else if (t instanceof SocketTimeoutException) {
                    errorCallback.onError(-99, "Failed to connect to server, please try again late!");
                } else {
                    System.out.println(t.getMessage().toString());
                    errorCallback.onError(-101, "Something went wrong, please retry!");
                }
            }

        });
    }


    public void updateHeader(String token) {
        if (token != null)
            authorization = "Bearer " + token;
        else
            authorization = null;
    }

   /* Call<JsonElement> authenticate(JsonObject jsonObject)
    {
        return retrofit.create(ApiInterface.class).createRide(jsonObject, authorization);
    }*/

    /*public void requestLogout(Context context) {
        if (context != null)
           // AuthHelper.clearData(context);
        if (authorization != null) {
            addRequestToQueue(logout(), new IServiceReceiver() {
                @Override
                public void onReceiver(JsonElement response) {

                }

            }, new IServiceError() {
                @Override
                public void onError(int code, String error) {
                }
            });
            updateHeader(null);
        }

    }*/
}
