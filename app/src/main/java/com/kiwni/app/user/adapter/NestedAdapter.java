package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.datamodels.BookingModel;
import com.kiwni.app.user.interfaces.BookingListItemClickListener;

import java.util.List;


public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.LayoutViewHolder> {

    List<BookingModel> mList;
    Context context;
    BookingModel bookingModel;

    private BookingListItemClickListener listener;

    public NestedAdapter(List<BookingModel> mList, BookingListItemClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item,parent,false);
        return new LayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayoutViewHolder holder, int position) {
        bookingModel = mList.get(position);
        holder.yearText.setText(bookingModel.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LayoutViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        AppCompatButton buttonBook;
        TextView yearText;
        public LayoutViewHolder(@NonNull View itemView) {
            super(itemView);

            buttonBook = itemView.findViewById(R.id.buttonBook);
            yearText = itemView.findViewById(R.id.yearText);

            buttonBook.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.buttonBook){
                listener.onItemClick(view, getAdapterPosition(), mList);
            }
        }
    }
}