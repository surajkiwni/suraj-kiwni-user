package com.kiwni.app.user.datamodels;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.ConfirmBookingActivity;
import com.kiwni.app.user.interfaces.ErrorDialogInterface;

public class ErrorDialog1
{
    ErrorDialogInterface errorDialogInterface;

    public void showError(Context context, String msg, ErrorDialogInterface errorDialogInterface)
    {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        AppCompatButton btnClose = dialog.findViewById(R.id.btnClose);
        TextView txtErrorMessage = dialog.findViewById(R.id.txtErrorMessage);

        txtErrorMessage.setText(msg);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                errorDialogInterface.onClick(context);
            }
        });

        dialog.show();
    }
}
