package com.kiwni.app.user.helpers;

import com.google.gson.JsonElement;

/**
 * Created by Sourav on 21/12/2021.
 */

public interface IServiceReceiver {
    void onReceiver(JsonElement response);

}
