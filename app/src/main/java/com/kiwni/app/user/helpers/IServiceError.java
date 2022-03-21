package com.kiwni.app.user.helpers;

/**
 * Created by Sourav on 21/12/2021.
 */

public interface IServiceError {
    void onError(int code, String error);
}
