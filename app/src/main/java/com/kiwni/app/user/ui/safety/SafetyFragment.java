package com.kiwni.app.user.ui.safety;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.SafetyFragmentBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;


public class SafetyFragment extends Fragment implements BackKeyPressedListener {

    private SafetyViewModel safetyViewModel;
    private SafetyFragmentBinding binding;
    String TAG = this.getClass().getSimpleName();
    Dialog updateAlertDialog, emergencyDialog;

    public static BackKeyPressedListener backKeyPressedListener;
    ImageView imgBack;
    AppCompatButton btnTryLater,alertBtn;
    TextView textPara2,textPara4,textPara5,txtTitle;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        safetyViewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        binding = SafetyFragmentBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        safetyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textPara2 =view.findViewById(R.id.textPara2);
        textPara4 = view.findViewById(R.id.textPara4);
        textPara5 = view .findViewById(R.id.textPara5);
        imgBack = view.findViewById(R.id.imgBack);
        txtTitle = view.findViewById(R.id.txtTitle);
        btnTryLater = view.findViewById(R.id.btnTryLater);
        alertBtn = view.findViewById(R.id.alertBtn);


        txtTitle.setText("Safety");

        alertBtn.setBackgroundColor(Color.BLACK);
        alertBtn.setTextColor(Color.WHITE);

        textPara2.setText(Html.fromHtml("Save the Mobile Number of up to <br/>\n" +
                "        2 person whom you trust.", Html.FROM_HTML_MODE_COMPACT));

        textPara4.setText(Html.fromHtml("Press Alert Button incase you need Help <br/>\n" +
                "        and wish to inform your dear ones", Html.FROM_HTML_MODE_COMPACT));

        textPara5.setText(Html.fromHtml("They will receive an alert buzzed and <br/>\n" +
                "        sms informing them about your location", Html.FROM_HTML_MODE_COMPACT));

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imgBack = view.findViewById(R.id.imageBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_safety_to_mainActivity);
            }
        });

        btnTryLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertBtn.setBackgroundResource(R.drawable.square_border);
                alertBtn.setTextColor(Color.BLACK);

                //tryLaterBtn.setBackgroundResource(R.drawable.square_border);
                btnTryLater.setBackgroundColor(Color.BLACK);
                btnTryLater.setTextColor(Color.WHITE);

                Navigation.findNavController(view).navigate(R.id.action_nav_safety_to_mainActivity);
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBtn.setBackgroundColor(Color.BLACK);
                alertBtn.setTextColor(Color.WHITE);

                btnTryLater.setBackgroundResource(R.drawable.square_border);
                btnTryLater.setTextColor(Color.BLACK);

                updateAlertDialog = new Dialog(getActivity(), android.R.style.Theme_Light);
                updateAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                updateAlertDialog.setContentView(R.layout.update_alert_details);

                final ImageView imageBack = updateAlertDialog.findViewById(R.id.imageBack);
                AppCompatButton btnSaveAlert = updateAlertDialog.findViewById(R.id.btnSaveAlert);
                AppCompatButton btnCancelUpdateAlert = updateAlertDialog.findViewById(R.id.btnCancelUpdateAlert);

                btnSaveAlert.setBackgroundColor(Color.BLACK);
                btnSaveAlert.setTextColor(Color.WHITE);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateAlertDialog.dismiss();
                    }
                });

                btnCancelUpdateAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateAlertDialog.dismiss();
                    }
                });

                btnSaveAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Log.d(TAG, "Clicked");
                        emergencyDialog = new Dialog(getActivity(), android.R.style.Theme_Light);
                        emergencyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        emergencyDialog.setContentView(R.layout.in_case_emergency);

                        ImageView imageBack = emergencyDialog.findViewById(R.id.imageBack);
                        ImageView imgPoliceCall = emergencyDialog.findViewById(R.id.imgPoliceCall);
                        ImageView imgKiwniHelpLineCall = emergencyDialog.findViewById(R.id.imgKiwniHelpLineCall);
                        ImageView imgCall = emergencyDialog.findViewById(R.id.imgCall);

                        imageBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                emergencyDialog.dismiss();
                            }
                        });

                        imgPoliceCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String phone = "7057052508";
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                startActivity(intent);
                            }
                        });

                        imgKiwniHelpLineCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String phone = "7057052508";
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                startActivity(intent);
                            }
                        });

                        imgCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String phone = "7057052508";
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                startActivity(intent);
                            }
                        });
                        emergencyDialog.show();
                    }
                });
                updateAlertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Navigation.findNavController(view).navigate(R.id.action_nav_safety_to_mainActivity);

    }

    @Override
    public void onPause() {
        backKeyPressedListener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        backKeyPressedListener = this;
    }
}