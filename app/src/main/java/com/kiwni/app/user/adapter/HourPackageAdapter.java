package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.HourPackage;

import java.util.List;

public class HourPackageAdapter extends RecyclerView.Adapter<HourPackageAdapter.HpViewHolder> {

    Context context;
    private final List<HourPackage> hourPackageModelList;

    public HourPackageAdapter(Context context, List<HourPackage> hourPackageModelList) {
        this.context = context;
        this.hourPackageModelList = hourPackageModelList;
    }

    @NonNull
    @Override
    public HpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.current_booking_recycler,parent,false);
        return new HpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HpViewHolder holder, int position) {

        HourPackage hourPackageModel = hourPackageModelList.get(position);

        holder.hourText.setText(hourPackageModel.getName());
        holder.kmText.setText(hourPackageModel.getKilometer());

    }

    @Override
    public int getItemCount() {
        return hourPackageModelList.size();
    }

    public class HpViewHolder extends RecyclerView.ViewHolder {

        TextView hourText,kmText;

        public HpViewHolder(@NonNull View itemView) {
            super(itemView);

            hourText = itemView.findViewById(R.id.hourText);
            kmText = itemView.findViewById(R.id.kmText);
        }
    }
}
