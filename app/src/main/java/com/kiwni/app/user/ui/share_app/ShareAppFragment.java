package com.kiwni.app.user.ui.share_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kiwni.app.user.databinding.FragmentShareAppBinding;


public class ShareAppFragment extends Fragment {

    private ShareAppViewModel shareAppViewModel;
    private FragmentShareAppBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareAppViewModel =
                new ViewModelProvider(this).get(ShareAppViewModel.class);

        binding = FragmentShareAppBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textShareApp;
        shareAppViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

}