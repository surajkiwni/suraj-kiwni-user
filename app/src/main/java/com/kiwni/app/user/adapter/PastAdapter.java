package com.kiwni.app.user.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.triphistory.TripsHistoryResp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.PastViewHolder> {
    Context context;
    List<TripsHistoryResp> tripHistoryList;
    TripsHistoryResp tripListResp;
    String startTimeFromString = "", startDateFromString = "";

    String TAG = this.getClass().getSimpleName();
    String splittedStr1 ="", splittedStr2 = "",
            splittedStr3 = "", splittedStr4 = "", concatDirection = "";

    public PastAdapter(Context context, List<TripsHistoryResp> tripHistoryList) {
        this.tripHistoryList = tripHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_recycler, parent, false);
        return new PastViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PastViewHolder holder, int position)
    {
        tripListResp = tripHistoryList.get(position);

        holder.txtPickupAddress.setText(tripListResp.getStartLocationCity());
        holder.txtDropAddress.setText(tripListResp.getEndlocationCity());
        holder.txtStatus.setText(tripListResp.getStatus());

        /*/split service type */
        GetClassTypeFromServiceType(tripListResp.getServiceType());
        holder.txtServiceType.setText(concatDirection + " Trip");

        if(tripListResp.getEstimatedPrice() != null)
        {
            holder.txtPrice.setText("Rs. " + Math.round(tripListResp.getEstimatedPrice()));
        }
        else
        {
            holder.txtPrice.setText("0");
        }

        getDateAndTimeFromString(tripListResp.getStartTime());
        holder.txtStartDateTime.setText(startDateFromString + " - " + startTimeFromString);

        getDateAndTimeFromString(tripListResp.getEndTime());
        holder.txtEndDateTime.setText(startDateFromString + " - " + startTimeFromString);
    }

    @Override
    public int getItemCount() {
        return tripHistoryList.size();
    }

    public class PastViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtStartDateTime, txtEndDateTime, txtPrice, txtPickupAddress, txtDropAddress,
                txtServiceType, txtStatus;

        public PastViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtStartDateTime = itemView.findViewById(R.id.txtStartDateTime);
            txtEndDateTime = itemView.findViewById(R.id.txtEndDateTime);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPickupAddress = itemView.findViewById(R.id.txtPickupAddress);
            txtDropAddress = itemView.findViewById(R.id.txtDropAddress);
            txtServiceType = itemView.findViewById(R.id.txtServiceType);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void getDateAndTimeFromString(String time)
    {
        Log.d("TAG", "str length = " + time.length());

        Date startDate = null;
        if (time.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                startDateFromString = sdf1.format(startDate);
                /*String updatedStr = String.valueOf(startDate);
                Log.d("TAG", "updatedStr = " + updatedStr);*/

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                startTimeFromString = sdf2.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else if (time.length() == 20) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                startDateFromString = sdf1.format(startDate);
                /*String updatedStr = String.valueOf(startDate);
                Log.d("TAG", "updatedStr = " + updatedStr);*/

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                startTimeFromString = sdf2.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else {
            Toast.makeText(context, "Given date is not in correct format.", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetClassTypeFromServiceType(String str)
    {
        String[] split = str.split("-");
        for (String s : split)
        {
            System.out.println(s);
            splittedStr1 = split[0];
            splittedStr2 = split[1];
            splittedStr3 = split[2];
            splittedStr4 = split[3];
        }
        Log.d(TAG, "data print from array = " + splittedStr1 + ", " + splittedStr2 +
                ", " + splittedStr3 + ", " + splittedStr4);

        concatDirection = splittedStr1.substring(0, 1).toUpperCase() + splittedStr1.substring(1).toLowerCase() + " " + splittedStr2.substring(0, 1).toUpperCase() + splittedStr2.substring(1).toLowerCase();
        Log.d(TAG, "concatDirection = " + concatDirection);
    }
}
