package com.kiwni.app.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kiwni.app.user.R;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.models.FindCar;
import com.kiwni.app.user.models.ScheduleMapResp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FindsCarRecyclerAdapter extends RecyclerView.Adapter<FindsCarRecyclerAdapter.ViewHolder>
{
    Context context;
    List<ScheduleMapResp> finalScheduleList = new ArrayList<>();
    List<String> listOfVehicleType = new ArrayList<>();
    FindCarItemClickListener listener;
    View view;

    List<ScheduleMapResp> selectedByVehicleTypeList = new ArrayList<>();
    List<ScheduleMapResp> tempList = new ArrayList<>();
    List<ScheduleMapResp> tempList1 = new ArrayList<>();
    List<ScheduleMapResp> remainingList = new ArrayList<>();

    boolean isEqual = false;

    public FindsCarRecyclerAdapter(Context context, List<String> listOfVehicleType, List<ScheduleMapResp> finalScheduleList, FindCarItemClickListener listener) {
        this.context = context;
        this.listOfVehicleType = listOfVehicleType;
        this.listener = listener;
        this.finalScheduleList = finalScheduleList;
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

        String str = listOfVehicleType.get(position);
        holder.carName.setText(str);

        Log.d("TAG", " car name = " + holder.carName.getText().toString());

        selectedByVehicleTypeList.clear();
        Log.d("TAG", "finalScheduleList size = " + finalScheduleList.size());
        for(int i = 0; i < finalScheduleList.size(); i++)
        {
            if(finalScheduleList.get(i).getVehicleType().equals(holder.carName.getText().toString()))
            {
                selectedByVehicleTypeList.add(finalScheduleList.get(i));
            }
        }

        holder.sortByModelAndClassType(selectedByVehicleTypeList);
        holder.txtAvailableCount.setText("Available " + tempList.size());

        int largeSize = selectedByVehicleTypeList.size() - 1;
        //Log.d("TAG", " list size for set = " + selectedByVehicleTypeList.size());
        Collections.sort(selectedByVehicleTypeList, orderModelPrice);
        String priceRange = "₹ " + Math.round(selectedByVehicleTypeList.get(0).getPrice()) + " - " + Math.round(selectedByVehicleTypeList.get(largeSize).getPrice());
        holder.txtPriceRange.setText(priceRange);

        //Log.d("TAG", "selected by vehicle type list size in adapter = " + selectedByVehicleTypeList.size());

        if(str.equals("SEDAN"))
        {
            Glide.with(context)
                    .load(R.drawable.sedan)
                    .into(holder.carImage);

            holder.txtSeatCapacity.setText("4 + " + "1 Seater");
        }
        else if(str.equals("SUV"))
        {
            Glide.with(context)
                    .load(R.drawable.suv)
                    .into(holder.carImage);

            holder.txtSeatCapacity.setText("5 + " + "1 Seater");
        }
        else
        {
            Glide.with(context)
                    .load(R.drawable.tempo_traveller)
                    .into(holder.carImage);
        }

    }

    @Override
    public int getItemCount() {
        return listOfVehicleType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView carName, txtAvailableCount, txtSeatCapacity, txtPriceRange;
        ConstraintLayout findCarLayout;
        ImageView carImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carName = itemView.findViewById(R.id.carNames);
            txtAvailableCount = itemView.findViewById(R.id.txtAvailableCount);
            txtSeatCapacity = itemView.findViewById(R.id.txtSeatCapacity);
            txtPriceRange = itemView.findViewById(R.id.txtPriceRange);
            carImage = itemView.findViewById(R.id.carImageView);
            findCarLayout= itemView.findViewById(R.id.findCarLayout);

            findCarLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    listener.onFindCarItemClick(view, getAdapterPosition(), listOfVehicleType,
                            carName.getText().toString(), txtSeatCapacity.getText().toString());
                }
            });

        }

        public void sortByModelAndClassType(List<ScheduleMapResp> list)
        {
            //Log.d("TAG", " list size in adapter = " + list.size());
            tempList.clear();
            tempList1.clear();

            int i = 0;
            while (i<=list.size())
            {
                for(int j = i+1; j < list.size(); j++)
                {
                    if(list.get(i).getClassType().equals(list.get(j).getClassType()))
                    {
                        if (list.get(i).getModel().equals(list.get(j).getModel()))
                        {
                            //System.out.println("model compare i = " + mList.get(i).getModel());
                            tempList.add(list.get(i));
                            Log.d("TAG", "temp list total size in if = " +tempList.size());

                            remainingList.add(list.get(j));
                            Log.d("TAG","remainingList = " +remainingList);

                            isEqual = true;
                        }
                    }
                    else
                    {
                        if (list.get(i).getModel().equals(list.get(j).getModel()))
                        {
                            //System.out.println("class type not same i " + selectedByVehicleTypeList.get(i).getModel());
                            //System.out.println("class type not same  j " + selectedByVehicleTypeList.get(j).getModel());

                            tempList.add(list.get(i));
                            tempList1.add(list.get(j));

                            tempList1.removeAll(tempList);
                            tempList.addAll(tempList1);

                            Log.d("TAG","temp list total size in else = " + tempList.size());
                        }
                    }
                }

                if (isEqual)
                {
                    i= i+2;
                    isEqual = false;
                }else{
                    i++;
                }
            }
        }
    }

    public static Comparator<ScheduleMapResp> orderModelPrice = new Comparator<ScheduleMapResp>() {
        @Override
        public int compare(ScheduleMapResp o1, ScheduleMapResp o2)
        {
            return o1.getPrice().compareTo(o2.getPrice());
        }
    };
}
