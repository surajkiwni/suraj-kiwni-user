package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.Support;

import java.util.List;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.SupportViewHolder> {

    private Context context;
    private List<Support> supportModels;

    public SupportAdapter(Context context, List<Support> supportModels) {
        this.context = context;
        this.supportModels = supportModels;
    }

    @NonNull
    @Override
    public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.support_recycler_item,parent,false);
        return new SupportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportViewHolder holder, int position) {

        Support supportModel = supportModels.get(position);

        holder.text1.setText(supportModel.getName());

    }

    @Override
    public int getItemCount() {
        return supportModels.size();
    }

    public class SupportViewHolder extends RecyclerView.ViewHolder {

        TextView text1;

        public SupportViewHolder(@NonNull View itemView) {
            super(itemView);

            text1= itemView.findViewById(R.id.text1);
        }
    }
}
