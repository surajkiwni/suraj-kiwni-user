package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.models.FindCar;

import java.util.List;

public class FindsCarRecyclerAdapter extends RecyclerView.Adapter<FindsCarRecyclerAdapter.ViewHolder>
{

    //String data[];
    Context context;
    List<FindCar> findCarModelList;
    FindCarItemClickListener findCarItemClickListener;
    View view;

    public FindsCarRecyclerAdapter(Context context, List<FindCar> findCarModelList, FindCarItemClickListener findCarItemClickListener) {
        this.context = context;
        this.findCarModelList = findCarModelList;
        this.findCarItemClickListener = findCarItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.one_way_custom_recycler_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       FindCar findCarModel = findCarModelList.get(position);

       holder.carName.setText(findCarModel.getCarName());
       holder.carImage.setImageResource(findCarModel.getImage());

    }

    @Override
    public int getItemCount() {
        return findCarModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView carName;
        ConstraintLayout findCarLayout;
        ImageView carImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carName = itemView.findViewById(R.id.carNames);
            carImage = itemView.findViewById(R.id.carImageView);
            findCarLayout= itemView.findViewById(R.id.findCarLayout);

            findCarLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.findCarLayout)
            {
                findCarItemClickListener.onFindCarItemClick(view,getAdapterPosition(),findCarModelList);
            }
        }
    }


}
