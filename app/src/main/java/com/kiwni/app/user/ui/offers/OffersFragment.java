package com.kiwni.app.user.ui.offers;

import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.OffersAdapter;
import com.kiwni.app.user.databinding.OffersFragmentBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;
import com.kiwni.app.user.models.Offers;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements BackKeyPressedListener {

    private OffersViewModel offersViewModel;
    private OffersFragmentBinding binding;
    View view;
    String TAG = this.getClass().getSimpleName();

    RecyclerView offersRecyclerView;
    List<Offers> offersModelList;
    public static BackKeyPressedListener backKeyPressedListener;
    ImageView imgBack;
    TextView txtTitle;

   // List<BookingModel> bookingModelList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        offersViewModel =
                new ViewModelProvider(this).get(OffersViewModel.class);

        binding = OffersFragmentBinding.inflate(inflater, container, false);
         view = binding.getRoot();

       // final TextView textView = binding.textGallery;
        offersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });



        return view;
    }

    @SuppressLint({"SetTextI18n", "WrongThread"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        offersRecyclerView = view.findViewById(R.id.offersRecyclerView);
        imgBack = view.findViewById(R.id.imgBack);
        txtTitle = view.findViewById(R.id.txtTitle);

        /*Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(R.drawable.offers));
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);*/

        txtTitle.setText("Offers");

        offersModelList = new ArrayList<>();
        //bookingModelList = new ArrayList<>();

        offersModelList.add(new Offers(R.drawable.offer1));
        offersModelList.add(new Offers(R.drawable.offer2));
        offersModelList.add(new Offers(R.drawable.offer3));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        offersRecyclerView.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL,false);
        offersRecyclerView.setLayoutManager(gridLayoutManager1);

        OffersAdapter offersAdapter = new OffersAdapter(getContext(),offersModelList);
        offersRecyclerView.setAdapter(offersAdapter);
        //offersAdapter.notifyDataSetChanged();


        ((MainActivity) requireActivity()).getSupportActionBar().hide();



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_offers_to_mainActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Navigation.findNavController(view).navigate(R.id.action_nav_offers_to_mainActivity);
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