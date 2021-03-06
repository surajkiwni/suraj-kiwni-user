package com.kiwni.app.user.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.Offers;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

    private final Context context;
    private final List<Offers> offersModels;

    public OffersAdapter(Context context, List<Offers> offersModels) {
        this.context = context;
        this.offersModels = offersModels;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.offers_recycler_item,parent,false);
        return new OffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {


        Offers offersModel =offersModels.get(position);

        holder.offersImage.setImageResource(offersModel.getImage());
        Log.d("TAG",offersModel.toString());

        //Glide.with(context).load(offersModel.getImage()).into(holder.offersImage);

    }

    @Override
    public int getItemCount() {
        return offersModels.size();
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder {

        ImageView offersImage;

        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);

            offersImage = itemView.findViewById(R.id.offersImage);
        }
    }
}
