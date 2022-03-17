package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.PastViewHolder> {

    String data2[];
    Context context;

    public PastAdapter(Context context, String[] data) {
        this.data2 = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PastAdapter.PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.past_recycler,parent,false);
        PastAdapter.PastViewHolder pastViewHolder = new PastAdapter.PastViewHolder(view);

        return pastViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PastAdapter.PastViewHolder holder, int position) {

        holder.pickupAdd.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data2.length;
    }

    public class PastViewHolder extends RecyclerView.ViewHolder {
        TextView pickupAdd;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);

            pickupAdd = itemView.findViewById(R.id.pickupAdd);
        }
    }
}
