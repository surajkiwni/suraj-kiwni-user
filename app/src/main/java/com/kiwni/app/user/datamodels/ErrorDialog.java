package com.kiwni.app.user.datamodels;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.kiwni.app.user.R;

public class ErrorDialog extends Dialog
{
    public ErrorDialog(@NonNull Context context, String title)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_error);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

        AppCompatButton btnClose = (AppCompatButton) findViewById(R.id.btnClose);
        TextView txtErrorMessage = (TextView) findViewById(R.id.txtErrorMessage);

        txtErrorMessage.setText(title);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
