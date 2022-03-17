package com.kiwni.app.user.ui.refer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kiwni.app.user.databinding.FragmentReferEarnBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;

public class ReferEarnFragment extends Fragment implements BackKeyPressedListener {


    private ReferEarnViewModel referEarnViewModel;
    private FragmentReferEarnBinding binding;

    public  static BackKeyPressedListener backKeyPressedListener;
    ImageView imageBack;
    View root;
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

        /*((MainActivity) requireActivity()).getSupportActionBar().hide();*/

        /*imageBack = root.findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_refer_to_mainActivity);
            }
        });*/
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
       // Navigation.findNavController(root).navigate(R.id.action_nav_refer_to_mainActivity);

    }
}