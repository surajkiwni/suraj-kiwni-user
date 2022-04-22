package com.kiwni.app.user.network;

public class AppConstants
{
    public static final String WEBSOCKET_RESERVATION_EVENT = "reservation_success";
    public static final String WEBSOCKET_DRIVER_DATA_EVENT = "driver_data_updated";
    public static final int SERVER_CONNECTION_TIMEOUT = 3000;
    public static final int SERVER_CONNECTION_ERROR = 3001;
    public static final int SERVER_CONNECTED = 3002;
    public static final int SERVER_DISCONNECTED = 3003;

    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int SPLASH_SCREEN_TIME_OUT = 2000;

    public static final String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ2NDExN2FjMzk2YmM3MWM4YzU5ZmI1MTlmMDEzZTJiNWJiNmM2ZTEiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiTmFsaW4gQ2hhbmRyYSBHaXJpIiwicGljdHVyZSI6Imh0dHA6Ly93d3cuZXhhbXBsZS5jb20vMTIzNDU2NzgvcGhvdG8ucG5nIiwiUm9sZXMiOlsiVVNFUiJdLCJwYXJ0eUlkIjoiMjY4IiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL2tpd25pLXBsYXRmb3JtIiwiYXVkIjoia2l3bmktcGxhdGZvcm0iLCJhdXRoX3RpbWUiOjE2NDg2MTgzMDQsInVzZXJfaWQiOiJVU0VSLWJkMGQ4YTg1LTAwYjQtNDlkNi1iZTE4LTgxYjA5NzlkNTU4NSIsInN1YiI6IlVTRVItYmQwZDhhODUtMDBiNC00OWQ2LWJlMTgtODFiMDk3OWQ1NTg1IiwiaWF0IjoxNjQ4NjE4MzA0LCJleHAiOjE2NDg2MjE5MDQsImVtYWlsIjoibmFsaW5Aa2l3bmkuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInBob25lX251bWJlciI6Iis5MTk1NzkzNjQ3MzUiLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIis5MTk1NzkzNjQ3MzUiXSwiZW1haWwiOlsibmFsaW5Aa2l3bmkuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.TPJqyUk1NKzan_-hu9WH60H-AResqqUoNar7bFkHQHXKIYH4PyYgCCz3HkXD5ZZKwReMi5CPQyGex5vfjXjkcAkiQUhLccYBtE-IYI7Zpt8VtEfX4Yx8ogpa4F8M5iKCFFKYhkmPDR411vf3o67kTPP3Ohh0gDR99bGjDFNBoPWJ7f1WfRUIn5u-nx3fqBwLGR0LL1nNxAfJGMpYpo_I3av8JBeE5-0ndsWaBd-JKuNjlgkpzG7yrMrFtjK2VTNO4DiRwrxbb7lmhWbsOFoe4UP51j-09i1TLVqZMcgg9gzQyNTcqEhff5h9hmAobYktIHZOMfx8pOINkn-xQSh7TQ";

    public static final String BASE_URL = "https://api.dev.kiwni.com/";

    public static final String IMAGE_PATH = "https://kiwni.com/car_images/";

    public static final String SOCKET_BASE_URL = "https://broadcast-service-7qkkocxieq-el.a.run.app/users";
}
