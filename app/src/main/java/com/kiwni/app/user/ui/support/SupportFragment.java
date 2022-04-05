package com.kiwni.app.user.ui.support;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.SupportAdapter;
import com.kiwni.app.user.databinding.FragmentSupportBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;
import com.kiwni.app.user.models.Support;

import java.util.ArrayList;
import java.util.List;

public class SupportFragment extends Fragment implements BackKeyPressedListener {

    private SupportViewModel supportViewModel;
    private FragmentSupportBinding binding;

    RecyclerView supportRecyclerView;
    List<Support> supportModelList;

    public  static BackKeyPressedListener backKeyPressedListener;
    View view;
    ImageView imageBack;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        supportViewModel =
                new ViewModelProvider(this).get(SupportViewModel.class);

        binding = FragmentSupportBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        supportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        supportRecyclerView = view.findViewById(R.id.supportRecyclerView);

        supportModelList= new ArrayList<>();

        supportModelList.add(new Support("Trips Issues and Refunds"));
        supportModelList.add(new Support("A guide to Kiwni"));
        supportModelList.add(new Support("Accessibility"));
        supportModelList.add(new Support("All about kiwni Service"));
        supportModelList.add(new Support("New Booking Error"));
        supportModelList.add(new Support("Kiwni emergency Number"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        supportRecyclerView.setLayoutManager(linearLayoutManager);

        SupportAdapter supportAdapter = new SupportAdapter(getContext(),supportModelList);
        supportRecyclerView.setAdapter(supportAdapter);


        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        imageBack = view.findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_support_to_mainActivity);
            }
        });

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
        //Navigation.findNavController(view).navigate(R.id.action_nav_support_to_mainActivity);

    }
}