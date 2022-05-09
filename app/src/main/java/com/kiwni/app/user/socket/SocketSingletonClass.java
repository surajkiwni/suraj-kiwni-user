package com.kiwni.app.user.socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.kiwni.app.user.models.socket.SocketReservationResp;
import com.kiwni.app.user.network.AppConstants;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketSingletonClass
{
    SocketSingletonClass socketSingleton;
    private Socket mSocket;
    String TAG = this.getClass().getSimpleName();
    Context context;
    SocketReservationResp reservationResp = new SocketReservationResp();
    boolean driver_data_updated = false;
    int partyId = 0;

    public SocketSingletonClass(int partyId) {
        this.partyId = partyId;

    }


    public SocketSingletonClass getInstance()
    {
        SocketConnect();
        return socketSingleton;
    }

    public void SocketConnect()
    {
        //Socket Listen
        try {
            mSocket = IO.socket(AppConstants.SOCKET_BASE_URL);
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT,onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);

            EmitData(partyId);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void EmitData(int partyId)
    {
        //{"partyId": 274}
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("partyId", partyId);
            mSocket.emit("join", obj);
            Log.d("join", obj.toString());

            ListenReservationMessage();

            ListenDriverDataMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void ListenReservationMessage()
    {
        mSocket.on(AppConstants.WEBSOCKET_RESERVATION_EVENT, onNewMessage);
    }

    public void ListenDriverDataMessage()
    {
        mSocket.on(AppConstants.WEBSOCKET_DRIVER_DATA_EVENT, onUpdatedMessage);
    }

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.e("IsConnected", String.valueOf(mSocket.connected()));
            Log.d(TAG, "connected to the server");
        }
    };

    private final Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connection error");
        }
    };

    private final Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "disconnected from the server");
        }
    };

    private final Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Log.d("MainActivity ", "in new msg");
                    Log.d("MainActivity ", "data = " + Arrays.toString(args));

                    if (args.length == 0)
                    {
                        //Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = (JSONObject) args[0];

                            Gson gson = new Gson();
                            reservationResp = gson.fromJson(jsonObject.toString(), SocketReservationResp.class);

                            Log.d(TAG, "reservationResp = " + reservationResp.toString());
                            Log.d(TAG, "reservationResp id = " + reservationResp.getId());

                            //DisplaySuccessDialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    private final Emitter.Listener onUpdatedMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Log.d("MainActivity ", "in updated msg");
                    Log.d("MainActivity ", "data = " + Arrays.toString(args));

                    driver_data_updated = true;

                    if (args.length == 0)
                    {
                        //Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = (JSONObject) args[0];

                            Gson gson = new Gson();
                            reservationResp = gson.fromJson(jsonObject.toString(), SocketReservationResp.class);

                            Log.d(TAG, "reservationResp = " + reservationResp.toString());
                            Log.d(TAG, "reservationResp id = " + reservationResp.getId());

                            //DisplaySuccessDialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
}
