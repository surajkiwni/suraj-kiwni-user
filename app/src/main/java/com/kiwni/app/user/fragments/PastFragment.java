package com.kiwni.app.user.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.GridLayoutWrapper;
import com.kiwni.app.user.adapter.PastAdapter;
import com.kiwni.app.user.classes.LoadingDialog;
import com.kiwni.app.user.interfaces.ErrorDialogInterface;
import com.kiwni.app.user.models.triphistory.TripsHistoryResp;
import com.kiwni.app.user.network.ApiClient;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastFragment extends Fragment implements ErrorDialogInterface
{
    RecyclerView pastRecyclerView;
    PastAdapter pastAdapter;
    List<TripsHistoryResp> tripHistoryList = new ArrayList<>();
    String TAG = this.getClass().getSimpleName();
    int partyId = 0;

    LoadingDialog loadingDialog;

    public PastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past, container, false);

        pastRecyclerView = view.findViewById(R.id.pastRecyclerView);

        loadingDialog = new LoadingDialog(getActivity());

        partyId = PreferencesUtils.getPreferences(getActivity(), SharedPref.partyId, 0);
        Log.d(TAG,"partyId = " + partyId);

        /* call trip history api */
        getTripHistoryData(partyId);

        return view;
    }

    public void getTripHistoryData(int id)
    {
        Log.d(TAG, "id = " + id);

        ApiInterface apiInterface = ApiClient.getClient(AppConstants.BASE_URL).create(ApiInterface.class);
        Call<List<TripsHistoryResp>> listCall = apiInterface.getPastTripHistory(id);

        // Set up progress before call
        loadingDialog.showLoadingDialog("Your request is processing");

        listCall.enqueue(new Callback<List<TripsHistoryResp>>() {
            @Override
            public void onResponse(Call<List<TripsHistoryResp>> call, Response<List<TripsHistoryResp>> response)
            {
                int statusCode = response.code();
                Log.d(TAG, "code = " + statusCode);
                Log.d(TAG, "response = " + response);

                loadingDialog.hideDialog();

                if(statusCode == 200)
                {
                    if (response.body().size() == 0)
                    {
                        //array is empty
                        Toast.makeText(getActivity(), R.string.trips_not_available_error, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        List<TripsHistoryResp> tempList = new ArrayList<>();

                        tripHistoryList = response.body();

                        pastRecyclerView.setHasFixedSize(true);
                        pastRecyclerView.setLayoutManager(new GridLayoutWrapper(getActivity(), 1));

                        for (int i = 0; i < tripHistoryList.size(); i++)
                        {
                            if(tripHistoryList.get(i).getStatus().equals("In-Progress"))
                            {
                                tempList.add(tripHistoryList.get(i));
                                //Log.d(TAG, "temp list size = " + tempList.size());
                            }
                        }

                        tripHistoryList.removeAll(tempList);

                        pastAdapter = new PastAdapter(getActivity(), tripHistoryList);
                        pastRecyclerView.setAdapter(pastAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TripsHistoryResp>> call, Throwable t)
            {
                Log.d(TAG, "error = " + t);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.hideDialog();
            }
        });
    }

    @Override
    public void onClick(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}