package com.kiwni.app.user.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    String data[];
    Context context;
    View view;

    public RecyclerAdapter(Context context,String[] data) {
        this.data = data;
        this.context = context;
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

        holder.textView.setText(data[position]);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView,toolbarText;
        ConstraintLayout constraintLayout,constraintLayout1,constraintLayout2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.carNames);
            toolbarText = itemView.findViewById(R.id.toolbarText);
            constraintLayout= itemView.findViewById(R.id.constraintLayout);
            constraintLayout1= itemView.findViewById(R.id.constraintLayout1);
            constraintLayout2= itemView.findViewById(R.id.constraintLayout2);


            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    //String mydata = toolbarText.getText().toString().trim();
                    String mydata = "One Way";
                    bundle.putString("outstation",mydata);
                    Log.d(TAG, "onClick: "+mydata);
                }
            });

            constraintLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    //String mydata = toolbarText.getText().toString().trim();
                    String mydata = "One Way";
                    bundle.putString("outstation",mydata);
                    Log.d(TAG, "onClick: "+mydata);
                }
            });

            constraintLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    //String mydata = toolbarText.getText().toString().trim();
                    String mydata = "One Way";
                    bundle.putString("outstation",mydata);
                    Log.d(TAG, "onClick: "+mydata);
                }
            });
        }
    }
}
