package com.kiwni.app.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.triphistory.TripsHistoryResp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomeViewHolder>
{
    Context context;
    List<TripsHistoryResp> tripHistoryList;
    TripsHistoryResp tripListResp;
    String startDateFromString = "", startTimeFromString = "", endDateFromString = "";

    public UpcomingAdapter(Context context, List<TripsHistoryResp> tripHistoryList) {
        this.tripHistoryList = tripHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public UpcomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_recycle, parent, false);
        return new UpcomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomeViewHolder holder, int position)
    {
        tripListResp = tripHistoryList.get(position);

        holder.txtPickupAddress.setText(tripListResp.getStartLocationCity());
        holder.txtDropAddress.setText(tripListResp.getEndlocationCity());
        holder.txtServiceType.setText(tripListResp.getServiceType());

        getDateFromString(tripListResp.getStartTime());
        getTimeFromString(tripListResp.getStartTime());
        getEndDateFromString(tripListResp.getEndTime());

        holder.txtDate.setText(startDateFromString + " - " + endDateFromString);
        holder.txtTime.setText(startTimeFromString);
    }

    @Override
    public int getItemCount() {
        return tripHistoryList.size();
    }

    public class UpcomeViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPickupAddress, txtDropAddress, txtDate, txtTime, txtServiceType, txtBookingNo, txtKRNNo;
        AppCompatButton btnCancelRide;

        public UpcomeViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtPickupAddress = itemView.findViewById(R.id.txtPickupAddress);
            txtDropAddress = itemView.findViewById(R.id.txtDropAddress);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtServiceType = itemView.findViewById(R.id.txtServiceType);
            txtBookingNo = itemView.findViewById(R.id.txtBookingNo);
            txtKRNNo = itemView.findViewById(R.id.txtKRNNo);
            btnCancelRide = (AppCompatButton) itemView.findViewById(R.id.btnCancelRide);

            btnCancelRide.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Navigation.findNavController(view).navigate(R.id.action_nav_myrides_to_mainActivity);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                    LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                    View dialogView = inflater.inflate(R.layout.reason_cancel_trip, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);
                    AlertDialog b = dialogBuilder.create();

                    b.show();

                    AppCompatButton btnDoNotCancel = (AppCompatButton) dialogView.findViewById(R.id.btnDoNotCancel);
                    AppCompatButton btnCancelRide = (AppCompatButton) dialogView.findViewById(R.id.btnCancelRide);

                    btnDoNotCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            b.dismiss();
                        }
                    });

                    btnCancelRide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());
                            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                            View dialogView1 = inflater.inflate(R.layout.booking_cancel_successful, null);
                            dialogBuilder.setView(dialogView1);
                            dialogBuilder.setCancelable(false);
                            AlertDialog b1 = dialogBuilder.create();

                            b1.show();
                            AppCompatButton btnOk = (AppCompatButton) dialogView1.findViewById(R.id.btnOk);

                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    b1.dismiss();
                                    b.dismiss();
                                    removeItem(getAdapterPosition());
                                    //Navigation.findNavController(view).navigate(R.id.action_upcomingFragment_to_mainActivity2);
                                }
                            });
                        }
                    });
                }
            });
        }

        public void removeItem(int newPosition)
        {
            newPosition = getAdapterPosition();
            tripHistoryList.remove(newPosition);
            notifyItemRemoved(newPosition);
            notifyItemRangeChanged(newPosition, tripHistoryList.size());
        }
    }

    public void getTimeFromString(String time)
    {
        Log.d("TAG", "str length = " + time.length());

        Date startDate = null;
        if (time.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(time);
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

    public void getDateFromString(String time)
    {
        Log.d("TAG", "str length = " + time.length());

        Date startDate = null;
        if (time.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM");
                startDateFromString = sdf1.format(startDate);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else if (time.length() == 20)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM");
                startDateFromString = sdf1.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else {
            Toast.makeText(context, "Given date is not in correct format.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getEndDateFromString(String time)
    {
        Log.d("TAG", "str length = " + time.length());

        Date startDate = null;
        if (time.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM");
                endDateFromString = sdf1.format(startDate);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else if (time.length() == 20)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                startDate = sdf.parse(time);
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM");
                endDateFromString = sdf1.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        } else {
            Toast.makeText(context, "Given date is not in correct format.", Toast.LENGTH_SHORT).show();
        }
    }
}
