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
import com.kiwni.app.user.interfaces.FilterItemClickListener;
import com.kiwni.app.user.models.Filter;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    Context context;
    List<Filter> filterModelList;
    FilterItemClickListener filterItemClickListener;

    public FilterAdapter(Context context, List<Filter> filterModelList, FilterItemClickListener filterItemClickListener) {
        this.context = context;
        this.filterModelList = filterModelList;
        this.filterItemClickListener = filterItemClickListener;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_filter_item,parent,false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {

        Filter filterModel = filterModelList.get(position);

        holder.filterImage.setImageResource(filterModel.getImage());
        holder.name.setText(filterModel.getName());

    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView filterImage;
        ConstraintLayout filterLayout;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);

            filterImage = itemView.findViewById(R.id.filterImage);
            name = itemView.findViewById(R.id.carName);
            filterLayout = itemView.findViewById(R.id.filterLayout);

            filterLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.filterLayout)
            {
                filterItemClickListener.onFilterItemClick(view, getAdapterPosition(), filterModelList);
            }
        }
    }
}
