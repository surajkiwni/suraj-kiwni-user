package com.kiwni.app.user.ui.FAQs;

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
import com.kiwni.app.user.databinding.FragmentFaqBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;


public class FaqFragment extends Fragment implements BackKeyPressedListener {

    private FaqViewModel faqViewModel;
    private FragmentFaqBinding binding;
    String TAG = this.getClass().getSimpleName();

    public static BackKeyPressedListener backKeyPressedListener;

    View view;
    ImageView imgBack;
    TextView txtTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        faqViewModel =
                new ViewModelProvider(this).get(FaqViewModel.class);

        binding = FragmentFaqBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        faqViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imgBack = view.findViewById(R.id.imgBack);
        txtTitle = view.findViewById(R.id.txtTitle);

        txtTitle.setText("FAQ");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_faq_to_mainActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Navigation.findNavController(view).navigate(R.id.action_nav_faq_to_mainActivity);
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
