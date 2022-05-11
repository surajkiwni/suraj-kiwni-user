package com.kiwni.app.user.classes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.kiwni.app.user.R;

public class LoadingDialog {

    Context context;
    Dialog dialog;

    public LoadingDialog(Context context){

        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    public void showLoadingDialog(String msg){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_progres_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtTitle.setText("Please Wait...");
        txtMessage.setText(msg);
        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}

