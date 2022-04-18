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
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.interfaces.BookBtnClickListener;
import com.kiwni.app.user.interfaces.ReviewBtnClickListener;
import com.kiwni.app.user.models.vehicle_details.ScheduleMapResp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.LayoutViewHolder> {

    List<ScheduleMapResp> mList = new ArrayList<>();
    Context context;
    BookBtnClickListener listener;
    ReviewBtnClickListener reviewBtnClickListener;
    String convertedTime = "";

    public NestedAdapter(Context context, List<ScheduleMapResp> mList, BookBtnClickListener listener, ReviewBtnClickListener reviewBtnClickListener) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
        this.reviewBtnClickListener = reviewBtnClickListener;
    }

    @NonNull
    @Override
    public LayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item,parent,false);
        return new LayoutViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LayoutViewHolder holder, int position) {
        ScheduleMapResp scheduleDatesResp = mList.get(position);

        getTimeFromDate(scheduleDatesResp.getVehicle().getRegYear());
        holder.txtRegYearNestedAdt.setText(convertedTime);

        holder.txtPriceNestedAdt.setText(Math.round(scheduleDatesResp.getPrice()) + " /-");
        holder.txtProviderNoNestedAdt.setText(scheduleDatesResp.getVehicle().getProvider().getName());

        Log.d("TAG","mList SizeNested = " +mList.size());
        Log.d("TAG","reg year = " + scheduleDatesResp.getVehicle().getRegYear());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LayoutViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        AppCompatButton btnBookNestedAdt;
        TextView txtRegYearNestedAdt,txtProviderNoNestedAdt, txtPriceNestedAdt,txtReviewNestedAdt;

        public LayoutViewHolder(@NonNull View itemView) {
            super(itemView);

            btnBookNestedAdt = itemView.findViewById(R.id.btnBookNestedAdt);
            txtRegYearNestedAdt = itemView.findViewById(R.id.txtRegYearNestedAdt);
            txtProviderNoNestedAdt = itemView.findViewById(R.id.txtProviderNoNestedAdt);
            txtPriceNestedAdt = itemView.findViewById(R.id.txtPriceNestedAdt);
            txtReviewNestedAdt = itemView.findViewById(R.id.txtReviewNestedAdt);

            btnBookNestedAdt.setOnClickListener(this);
            txtReviewNestedAdt.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (view.getId() == R.id.btnBookNestedAdt)
            {
                listener.onBookBtnClick(view, getAdapterPosition(), mList);
            }

            if (view.getId() == R.id.txtReviewNestedAdt)
            {
                reviewBtnClickListener.onReviewBtnClick(view, getAdapterPosition());
            }
        }
    }

    public void getTimeFromDate(String actual_date)
    {
        Log.d("TAG", "str length = " + actual_date.length());

        Date startDate = null;
        if (actual_date.length() == 28)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss.sss'Z'");
                startDate = sdf.parse(actual_date);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
                convertedTime = sdf2.format(startDate);
                Log.d("TAG", "convertedTime  = " + convertedTime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        }
        else if (actual_date.length() == 27)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss.sss'Z'");
                startDate = sdf.parse(actual_date);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
                convertedTime = sdf2.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "message = " + e.getMessage());
            }
        }
        else {
            Toast.makeText(context, "Given date is not in correct format.", Toast.LENGTH_SHORT).show();
        }

    }
}