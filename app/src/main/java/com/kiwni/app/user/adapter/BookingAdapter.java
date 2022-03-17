package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.kiwni.app.user.R;
import com.kiwni.app.user.datamodels.BookingModel2;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<BookingModel2> bookingModels;

    public BookingAdapter(Context context, List<BookingModel2> bookingModels) {
        this.context = context;
        this.bookingModels = bookingModels;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.booking_list_item1,parent,false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {

        BookingModel2 bookingModel = bookingModels.get(position);

        holder.name.setText(bookingModel.getName());
        holder.mImage.setImageResource(bookingModel.getImage());

    }

    @Override
    public int getItemCount() {
        return bookingModels.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView mImage;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameText);
            mImage = itemView.findViewById(R.id.image1);
        }
    }
}
