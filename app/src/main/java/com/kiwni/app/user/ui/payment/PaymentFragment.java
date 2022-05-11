package com.kiwni.app.user.ui.payment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.FragmentPaymentBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;


public class PaymentFragment extends Fragment implements BackKeyPressedListener {

    private PaymentViewModel paymentViewModel;
    private FragmentPaymentBinding binding;
    String TAG = this.getClass().getSimpleName();

    View view;
    TextView txtTitle;
    ImageView imgBack;
    public static BackKeyPressedListener backKeyPressedListener;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paymentViewModel =
                new ViewModelProvider(this).get(PaymentViewModel.class);

        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //final TextView textView = binding.textPayment;
        paymentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imgBack = view.findViewById(R.id.imgBack);
        txtTitle = view.findViewById(R.id.txtTitle);

        txtTitle.setText("Payment");


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_payment_to_mainActivity);
            }
        });

        return view;
    }

    @Override
    public void onBackPressed() {
        Navigation.findNavController(view).navigate(R.id.action_nav_payment_to_mainActivity);

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