package com.kiwni.app.user.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.kiwni.app.user.network.AppConstants;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

@SuppressLint("StaticFieldLeak")
public class SocketManager
{
    //private static SocketManager mInstance;
    private static SocketManager mInstance;
    private Socket mSocket;
    private final int RECONNECTION_ATTEMPT = 10;
    private final long CONNECTION_TIMEOUT = 30000;
   // private static NetworkInterface mNetworkInterface;
    Context context;

    public static SocketManager getInstance(Context context) {      //, NetworkInterface interfaces
        //mNetworkInterface = interfaces;
        context = context;
        if (mInstance == null) {
            mInstance = new SocketManager();
        }
        return mInstance;
    }

    /**
     * The purpose of this method to create the socket object
     */
    public void connectToSocket() {
        try {
            /*IO.Options opts = new IO.Options();
            opts.timeout = CONNECTION_TIMEOUT;
            opts.reconnection = true;
            opts.reconnectionAttempts = RECONNECTION_ATTEMPT;
            opts.reconnectionDelay = 1000;
            opts.forceNew = true;*/
            mSocket = IO.socket(AppConstants.SOCKET_BASE_URL);
           /*mSocket = IO.socket(NetworkConstant.SOCKET_CONNECTION_URL);
            mSocket.io().timeout(CONNECTION_TIMEOUT);
            mSocket.io().reconnection(true);
            mSocket.io().reconnectionAttempts(RECONNECTION_ATTEMPT);*/
            makeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of the method is to return the instance of socket
     *
     * @return
     */
    public Socket getSocket() {
        return mSocket;
    }

    /**
     * The purpose of this method is to connect with the socket
     */

    public void makeConnection() {
        mSocket.connect();
        registerConnectionAttributes();
    }
    /**
     * The purpose of this method is to disconnect from the socket interface
     */
    public void disconnectFromSocket() {
        unregisterConnectionAttributes();
        mSocket.disconnect();
        mSocket = null;
        mInstance = null;
    }

    public void registerConnectionAttributes() {
        try {
            if(mSocket.connected()) {
                mSocket.on(Socket.EVENT_CONNECT, onConnect);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionTimeOut);
                mSocket.on(Socket.EVENT_DISCONNECT, onServerDisconnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void unregisterConnectionAttributes() {
        try {
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
            //mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
            mSocket.off(Socket.EVENT_DISCONNECT, onServerDisconnect);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method to get the call back for connection getting timed out
     */
    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Response", "onConnect");
            //mNetworkInterface.networkCallReceive(AppConstants.SERVER_CONNECTED);
        }
    };

    /**
     * The purpose of this method is to get the call back for any type of connection error
     */
    private final Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Response", "onConnectionError");
            //mNetworkInterface.networkCallReceive(AppConstants.SERVER_CONNECTION_ERROR);
        }
    };

    /**
     * The purpose of this method to get the call back for connection getting timed out
     */
    private final Emitter.Listener onConnectionTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Response", "onConnectionTimeOut");
            //mNetworkInterface.networkCallReceive(AppConstants.SERVER_CONNECTION_TIMEOUT);
        }
    };
    /**
     * The purpose of this method is to receive the call back when the server get disconnected
     */
    private final Emitter.Listener onServerDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("Response", "onServerDisconnection");
            //mNetworkInterface.networkCallReceive(AppConstants.SERVER_DISCONNECTED);
        }
    };

    /**
     * The purpose of this method is register a method on server
     *
     * @param methodOnServer
     * @param handlerName
     */
    public void registerHandler(String methodOnServer, Emitter.Listener handlerName) {
        try {
            if(mSocket.connected())
                mSocket.on(methodOnServer, handlerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method is to unregister a method from server
     *
     * @param methodOnServer
     * @param handlerName
     */
    public void unRegisterHandler(String methodOnServer, Emitter.Listener handlerName) {
        try {
            mSocket.off(methodOnServer, handlerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The purpose of this method is to send the data to the server
     *
     * @param methodOnServer
     * @param request
     */
    public void sendDataToServer(String methodOnServer, JSONObject request) {
        Log.d("JSON ", request.toString());
        try {
            if(mSocket.connected())
            {
                mSocket.emit(methodOnServer, request);
            }
            else
            {
                //mNetworkInterface.networkCallReceive(AppConstants.SERVER_CONNECTION_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*public interface NetworkInterface {
        void networkCallReceive(int responseType);
    }*/
}
