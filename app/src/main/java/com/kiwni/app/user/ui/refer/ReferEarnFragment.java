package com.kiwni.app.user.ui.refer;

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
import com.kiwni.app.user.databinding.FragmentReferEarnBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;

public class ReferEarnFragment extends Fragment implements BackKeyPressedListener {


    private ReferEarnViewModel referEarnViewModel;
    private FragmentReferEarnBinding binding;
    String TAG = this.getClass().getSimpleName();

    public  static BackKeyPressedListener backKeyPressedListener;
    ImageView imgBack;
    TextView txtTitle;
    View root;
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        referEarnViewModel =
                new ViewModelProvider(this).get(ReferEarnViewModel.class);

        binding = FragmentReferEarnBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        //final TextView textView = binding.textReferEarn;
        referEarnViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imgBack = root.findViewById(R.id.imgBack);
        txtTitle = root.findViewById(R.id.txtTitle);

        txtTitle.setText("Refer &amp; Earn");



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_refer_to_mainActivity);
            }
        });
        return root;
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

    @Override
    public void onBackPressed() {
        Navigation.findNavController(root).navigate(R.id.action_nav_refer_to_mainActivity);

    }
}