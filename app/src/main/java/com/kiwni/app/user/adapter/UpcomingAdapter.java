package com.kiwni.app.user.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kiwni.app.user.R;
import com.kiwni.app.user.models.triphistory.TripsHistoryResp;
import com.kiwni.app.user.network.AppConstants;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
    byte[] valueDecoded= new byte[0];
    String TAG = this.getClass().getSimpleName();
    String splittedStr1 = "", splittedStr2 = "", splittedStr3 = "", splittedStr4 = "",
            concatDirection = "";

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UpcomeViewHolder holder, int position)
    {
        tripListResp = tripHistoryList.get(position);

        holder.txtPickupAddress.setText(tripListResp.getStartLocationCity());
        holder.txtDropAddress.setText(tripListResp.getEndlocationCity());
        holder.txtKRNNo.setText("KRN. No. KIWNI " + tripListResp.getReservationId());
        holder.txtBookingNo.setText("Booking No. -");

        /* split string and set data to ui */
        GetClassTypeFromServiceType(tripListResp.getServiceType());
        holder.txtServiceType.setText(" " + splittedStr3);

        getDateFromString(tripListResp.getStartTime());
        getTimeFromString(tripListResp.getStartTime());
        getEndDateFromString(tripListResp.getEndTime());

        concatDirection = splittedStr1 + "-" + splittedStr2;

        if(concatDirection.equals("one-way"))
        {
            holder.txtDate.setText(startDateFromString);
        }
        else                                            //if(concatDirection.equals("two-way"))
        {
            holder.txtDate.setText(startDateFromString + " - " + endDateFromString);
        }

        //display start time
        holder.txtTime.setText(startTimeFromString);

        if(tripListResp.getDriver() == null)
        {
            Log.d(TAG, "not getting driver details");
        }
        else
        {
            if(!tripListResp.getDriver().getName().equals("") &&
                    !tripListResp.getDriver().getName().equals("null"))
            {
                holder.driverConstraintLayout.setVisibility(View.VISIBLE);
                holder.bookingLinearLayout.setVisibility(View.GONE);
                holder.txtMessage.setVisibility(View.GONE);
                holder.v.setVisibility(View.GONE);

                /* set data to view(ui) */
                holder.txtDriverName.setText(tripListResp.getDriver().getName());
                holder.txtDriverMobile.setText(tripListResp.getDriver().getMobile());
                holder.txtVehicleNo.setText(tripListResp.getVehcileNo());
                holder.txtVehicleModel.setText("Vehicle Model : ");

                if(splittedStr4.equals("ultra"))
                {
                    holder.txtVehicleClassType.setText(splittedStr4 + "-luxury");
                }
                else
                {
                    holder.txtVehicleClassType.setText(splittedStr4);
                }

                /* decode otp and then set to ui */
                DecodeOtp(tripListResp.getOtp());
                holder.txtOTP.setText("OTP : " + new String(valueDecoded));

                /* set image to ui */
                if(tripListResp.getDriverImageUrl() != null)
                {
                    Glide.with(context)
                            .load(AppConstants.IMAGE_PATH + tripListResp.getDriverImageUrl())
                            .into(holder.imgDriverImg);
                }
            }
            else
            {
                holder.driverConstraintLayout.setVisibility(View.GONE);
                holder.bookingLinearLayout.setVisibility(View.VISIBLE);
                holder.txtMessage.setVisibility(View.VISIBLE);
                holder.v.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tripHistoryList.size();
    }

    public class UpcomeViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtPickupAddress, txtDropAddress, txtDate, txtTime, txtServiceType,
                txtBookingNo, txtKRNNo, txtMessage, txtVehicleClassType, txtVehicleModel,
                txtDriverMobile, txtVehicleNo, txtOTP, txtDriverName;
        AppCompatButton btnCancelRide;
        LinearLayout bookingLinearLayout;
        ConstraintLayout driverConstraintLayout;
        View v;
        ImageView imgDriverImg;

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
            btnCancelRide = itemView.findViewById(R.id.btnCancelRide);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtVehicleClassType = itemView.findViewById(R.id.txtVehicleClassType);
            txtVehicleModel = itemView.findViewById(R.id.txtVehicleModel);
            txtDriverMobile = itemView.findViewById(R.id.txtDriverMobile);
            txtVehicleNo = itemView.findViewById(R.id.txtVehicleNo);
            txtOTP = itemView.findViewById(R.id.txtOTP);
            txtDriverName = itemView.findViewById(R.id.txtDriverName);
            bookingLinearLayout = itemView.findViewById(R.id.bookingLinearLayout);
            driverConstraintLayout = itemView.findViewById(R.id.driverConstraintLayout);
            v = itemView.findViewById(R.id.view);
            imgDriverImg = itemView.findViewById(R.id.imgDriverImg);

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

                    AppCompatButton btnDoNotCancel = dialogView.findViewById(R.id.btnDoNotCancel);
                    AppCompatButton btnCancelRide = dialogView.findViewById(R.id.btnCancelRide);

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
                            AppCompatButton btnOk = dialogView1.findViewById(R.id.btnOk);

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

    @SuppressLint("SimpleDateFormat")
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

    @SuppressLint("SimpleDateFormat")
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

    @SuppressLint("SimpleDateFormat")
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

    /* decode otp */
    public void DecodeOtp(String otp)
    {
        //convert using Base64 decoder
        valueDecoded = Base64.decode(otp.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        Log.d(TAG, "valueDecoded = " + new String(valueDecoded));
    }

    /* separate class type from service type and set to ui */
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

    }
}
