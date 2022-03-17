package com.kiwni.app.user.ui.about;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.FragmentAboutBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;


public class AboutFragment extends Fragment implements BackKeyPressedListener {

    private aboutViewModel aboutViewModel;
    private FragmentAboutBinding binding;

    public static  BackKeyPressedListener backKeyPressedListener;
    ImageView imageBack;
    TextView text1,text2;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        aboutViewModel =
                new ViewModelProvider(this).get(aboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        view = binding.getRoot();

       // final TextView textView = binding.textAbout;
        aboutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

/*
        ((MainActivity) requireActivity()).getSupportActionBar().hide();
*/

        text1= view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);

        text1.setText(Html.fromHtml("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;We Are Kiwni............KIWINI CLUB, is powered \n" +
                "by Komfrey Technologies Pvt Ltd which is an Indian \n" +
                "online Start-up car rental company founded as an \n" +
                "online cab aggregator in Pune catering some of the \n" +
                "top class Vendors which relate well to our target \n" +
                "audience interest for outstation which helps you \n" +
                "find a car by experiencing luxury with comfort at a \n" +
                "reasonable fare. </p>", Html.FROM_HTML_MODE_COMPACT));

        text2.setText(Html.fromHtml("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;We offer a world class car rental application-\n" +
                "based services to people through a user-friendly \n" +
                "online booking application. We offer car with well trained \n" +
                "and certified drivers with well maintained \n" +
                "cars with a choice to choose as per your \n" +
                "convenient.</p>", Html.FROM_HTML_MODE_COMPACT));

        /*imageBack = view.findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_nav_about_to_mainActivity);
            }
        });*/

        /*final NavController navController = Navigation.findNavController(root);

        navController.navigate(R.id.action_findsCarOneWayFragment_to_mainActivity);
*/
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
       // Navigation.findNavController(view).navigate(R.id.action_nav_about_to_mainActivity);

    }
}