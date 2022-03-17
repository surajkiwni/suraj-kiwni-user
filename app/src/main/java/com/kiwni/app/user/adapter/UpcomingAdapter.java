package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomeViewHolder> {
    String data2[];
    Context context;

    public UpcomingAdapter(Context context, String[] data) {
        this.data2 = data;
        this.context = context;
    }

    @NonNull
    @Override
    public UpcomingAdapter.UpcomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upcoming_recycle,parent,false);
        UpcomeViewHolder upcomeViewHolder = new UpcomeViewHolder(view);
        return upcomeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.UpcomeViewHolder holder, int position) {

        holder.carModel.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data2.length;
    }

    public class UpcomeViewHolder extends RecyclerView.ViewHolder {

        TextView carModel;
        AppCompatButton cancelRideButton;

        public UpcomeViewHolder(@NonNull View itemView) {
            super(itemView);

            carModel = itemView.findViewById(R.id.CarModel);
            cancelRideButton = (AppCompatButton) itemView.findViewById(R.id.cancelRideButton);


            /*final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            final NavController navController = navHostFragment.getNavController();*/



            cancelRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Navigation.findNavController(view).navigate(R.id.action_nav_myrides_to_mainActivity);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                View dialogView = inflater.inflate(R.layout.reason_cancel_trip, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                b.show();

                AppCompatButton dontCancelButton = (AppCompatButton)  dialogView.findViewById(R.id.dontCancelButton);
                AppCompatButton cancelRideButton = (AppCompatButton) dialogView.findViewById(R.id.cancelRideButton);

                dontCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });

                cancelRideButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());
                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                        View dialogView1 = inflater.inflate(R.layout.booking_cancel_successful, null);
                        dialogBuilder.setView(dialogView1);
                        dialogBuilder.setCancelable(false);
                        AlertDialog b1 = dialogBuilder.create();

                        b1.show();
                        AppCompatButton okButton = (AppCompatButton) dialogView1.findViewById(R.id.okButton);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                b1.dismiss();
                                b.dismiss();
                                //Navigation.findNavController(view).navigate(R.id.action_upcomingFragment_to_mainActivity2);
                            }
                        });
                    }
                });
            }
        });

        }



    }
}
