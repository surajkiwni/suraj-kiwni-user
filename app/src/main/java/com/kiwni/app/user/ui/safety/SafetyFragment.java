package com.kiwni.app.user.ui.safety;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

    public static BackKeyPressedListener backKeyPressedListener;
    ImageView imageBack;
    AppCompatButton tryLaterBtn,alertBtn;
    TextView textPara2,textPara4,textPara5;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        safetyViewModel =
                new ViewModelProvider(this).get(SafetyViewModel.class);

        binding = SafetyFragmentBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        safetyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textPara2 =view.findViewById(R.id.textPara2);
        textPara4 = view.findViewById(R.id.textPara4);
        textPara5 = view .findViewById(R.id.textPara5);

        tryLaterBtn = view.findViewById(R.id.tryLaterBtn);
        alertBtn = view.findViewById(R.id.alertBtn);

        alertBtn.setBackgroundColor(Color.BLACK);
        alertBtn.setTextColor(Color.WHITE);

        textPara2.setText(Html.fromHtml("Save the Mobile Number of up to <br/>\n" +
                "        2 person whom you trust.", Html.FROM_HTML_MODE_COMPACT));

        textPara4.setText(Html.fromHtml("Press Alert Button incase you need Help <br/>\n" +
                "        and wish to inform your dear ones", Html.FROM_HTML_MODE_COMPACT));

        textPara5.setText(Html.fromHtml("They will receive an alert buzzed and <br/>\n" +
                "        sms informing them about your location", Html.FROM_HTML_MODE_COMPACT));

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imageBack = view.findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_safety_to_mainActivity);
            }
        });

        tryLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertBtn.setBackgroundResource(R.drawable.square_border);
                alertBtn.setTextColor(Color.BLACK);

                //tryLaterBtn.setBackgroundResource(R.drawable.square_border);
                tryLaterBtn.setBackgroundColor(Color.BLACK);
                tryLaterBtn.setTextColor(Color.WHITE);
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //alertBtn.setBackgroundResource(R.drawable.square_border);
                alertBtn.setBackgroundColor(Color.BLACK);
                alertBtn.setTextColor(Color.WHITE);

                tryLaterBtn.setBackgroundResource(R.drawable.square_border);
                //tryLaterBtn.setBackgroundColor(Color.BLACK);
                tryLaterBtn.setTextColor(Color.BLACK);

                Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.update_alert_details);

                final ImageView imageBack = dialog.findViewById(R.id.imageBack);
                AppCompatButton btnSaveAlert = dialog.findViewById(R.id.btnSaveAlert);
                AppCompatButton btnCancelUpdateAlert = dialog.findViewById(R.id.btnCancelUpdateAlert);

                btnSaveAlert.setBackgroundColor(Color.BLACK);
                btnSaveAlert.setTextColor(Color.WHITE);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnCancelUpdateAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnSaveAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.in_case_emergency);

                        //dialog.show();

                        ImageView imageBack = dialog.findViewById(R.id.imageBack);
                        ImageView imgPoliceCall = dialog.findViewById(R.id.imgPoliceCall);
                        ImageView imgKiwniHelpLineCall = dialog.findViewById(R.id.imgKiwniHelpLineCall);
                        ImageView imgCall = dialog.findViewById(R.id.imgCall);

                        imageBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
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
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Navigation.findNavController(view).navigate(R.id.action_nav_safety_to_mainActivity);

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