package com.kiwni.app.user.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.GridLayoutWrapper;
import com.kiwni.app.user.adapter.PastAdapter;
import com.kiwni.app.user.adapter.UpcomingAdapter;
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.models.triphistory.TripsHistoryResp;
import com.kiwni.app.user.network.ApiClient;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends Fragment
{
    RecyclerView upcomingRecyclerView;
    UpcomingAdapter upcomingAdapter;
    List<TripsHistoryResp> tripHistoryList = new ArrayList<>();
    String TAG = this.getClass().getSimpleName();
    int partyId = 0;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        upcomingRecyclerView = view.findViewById(R.id.upcomingRecyclerView);

        partyId = PreferencesUtils.getPreferences(getActivity(), SharedPref.partyId, 0);
        Log.d(TAG,"partyId = " + partyId);

        /* call trip history api */
        if(!isNetworkConnected())
        {
            ErrorDialog errorDialog = new ErrorDialog(getActivity(), "No internet. Connect to wifi or cellular network.");
            errorDialog.show();
            //Toast.makeText(getActivity(), "No internet. Connect to wifi or cellular network.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            getTripHistoryData(partyId);
        }

        return view;
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void getTripHistoryData(int id)
    {
        Log.d(TAG, "id = " + id);

        ApiInterface apiInterface = ApiClient.getClient(AppConstants.BASE_URL).create(ApiInterface.class);
        Call<List<TripsHistoryResp>> listCall = apiInterface.getUpcomingTripHistory(id, "In-Progress");

        // Set up progress before call
        Dialog lovelyProgressDialog = new LovelyProgressDialog(getActivity())
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle(R.string.connecting_to_server)
                .setMessage(R.string.your_request_is_processing)
                .setTopColorRes(R.color.teal_200)
                .show();

        listCall.enqueue(new Callback<List<TripsHistoryResp>>() {
            @Override
            public void onResponse(Call<List<TripsHistoryResp>> call, Response<List<TripsHistoryResp>> response)
            {
                int statusCode = response.code();
                Log.d(TAG, "code = " + statusCode);
                Log.d(TAG, "response = " + response.toString());

                lovelyProgressDialog.dismiss();

                if(statusCode == 200)
                {
                    if (response.body().size() == 0)
                    {
                        //array is empty
                        Toast.makeText(getActivity(), R.string.trips_not_available_error, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        tripHistoryList = response.body();
                        Log.d(TAG, "size = " + tripHistoryList.size());

                        upcomingRecyclerView.setHasFixedSize(true);
                        upcomingRecyclerView.setLayoutManager(new GridLayoutWrapper(getActivity(), 1));

                        upcomingAdapter = new UpcomingAdapter(getActivity(), tripHistoryList);
                        upcomingRecyclerView.setAdapter(upcomingAdapter);
                        upcomingAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TripsHistoryResp>> call, Throwable t)
            {
                Log.d(TAG, "error = " + t.toString());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                lovelyProgressDialog.dismiss();
            }
        });
    }
}