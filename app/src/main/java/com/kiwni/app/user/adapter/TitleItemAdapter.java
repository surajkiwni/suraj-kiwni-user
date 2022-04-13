package com.kiwni.app.user.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.interfaces.BookingListItemClickListener;
import com.kiwni.app.user.models.ScheduleMapResp;

import java.util.ArrayList;
import java.util.List;

public class TitleItemAdapter extends RecyclerView.Adapter<TitleItemAdapter.TitleViewHolder>
{
    List<ScheduleMapResp> mList = new ArrayList<>();
    BookingListItemClickListener listener;
    List<ScheduleMapResp> appendList = new ArrayList<>();
    List<ScheduleMapResp> remainingList = new ArrayList<>();
    Context context;

    public TitleItemAdapter(Context context, List<ScheduleMapResp> mList, List<ScheduleMapResp> remainingList, BookingListItemClickListener listener){
        this.context  = context;
        this.mList  = mList;
        this.listener = listener;
        this.remainingList = remainingList;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_each_item,parent,false);
        return new TitleViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position)
    {
        ScheduleMapResp scheduleDatesRespModel = mList.get(position);
        holder.txtCarModel.setText(scheduleDatesRespModel.getModel());
        holder.txtClassType.setText(scheduleDatesRespModel.getClassType());

        boolean isExpandable = scheduleDatesRespModel.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        //compare model and class type and set child data accordingly
        appendList.clear();
        for (int i = 0; i < mList.size(); i++)
        {
            if (mList.get(i).getVehicle().getModel().equals(holder.txtCarModel.getText().toString()))
            {
                Log.d("TAG", "print list of model = " + mList.get(i).getModel());
                //appendList.add(mList.get(i));
                if (mList.get(i).getClassType().equals(holder.txtClassType.getText().toString()))
                {
                    Log.d("TAG", "print list of class type = " + mList.get(i).getClassType());

                    mList.removeAll(appendList);
                    appendList.add(mList.get(i));

                    holder.txtAvailableCount.setText("Available  " + appendList.size());

                    //System.out.println("price = " + );
                }
            }
            else
            {
                Log.d("TAG", "Not Match Condition: ");
            }
        }

        for (int i = 0; i < remainingList.size(); i++)
        {
            if (remainingList.get(i).getVehicle().getModel().equals(holder.txtCarModel.getText().toString()))
            {
                Log.d("TAG", "print list of model = " + remainingList.get(i).getModel());
                //appendList.add(mList.get(i));
                if (remainingList.get(i).getVehicle().getClassType().equals(holder.txtClassType.getText().toString()))
                {
                    Log.d("TAG", "print list of class type = " + remainingList.get(i).getClassType());

                    appendList.add(remainingList.get(i));
                    holder.txtAvailableCount.setText("Available  " + appendList.size());
                }
            } else {
                Log.d("TAG", "Not Match Condition: ");
            }
        }

        // pass response to the nested recycler view
        // same list pass to the nested adapter
        NestedAdapter adapter = new NestedAdapter(context, appendList,listener);
        Log.d("TAG","append list = " + appendList.size());

        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.constraintLayoutTitleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                scheduleDatesRespModel.setExpandable(!scheduleDatesRespModel.isExpandable());
                // list = scheduleDatesRespModel.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout constraintLayoutTitleList, expandableLayout;
        TextView txtCarModel,txtClassType,txtAvailableCount,txtPriceRange;
        RecyclerView nestedRecyclerView;

        public TitleViewHolder(@NonNull View itemView)
        {
            super(itemView);

            constraintLayoutTitleList = itemView.findViewById(R.id.constraintLayoutTitleList);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            txtCarModel = itemView.findViewById(R.id.txtCarModel);
            txtAvailableCount = itemView.findViewById(R.id.txtAvailableCount);
            txtPriceRange = itemView.findViewById(R.id.txtPriceRange);
            txtClassType = itemView.findViewById(R.id.txtClassType);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
        }
    }
}
