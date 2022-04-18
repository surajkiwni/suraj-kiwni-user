package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.models.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

public class DialogReviewAdapter extends RecyclerView.Adapter<DialogReviewAdapter.ReviewViewHolder> {

    Context context;
    List<ReviewResponse> reviewResponseList = new ArrayList<>();

    public DialogReviewAdapter(Context context, List<ReviewResponse> reviewResponseList) {
        this.context = context;
        this.reviewResponseList = reviewResponseList;
    }

    @NonNull
    @Override
    public DialogReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_layout, parent, false);
        return new DialogReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogReviewAdapter.ReviewViewHolder holder, int position) {

        ReviewResponse reviewResponse = reviewResponseList.get(position);

        holder.txtName.setText(reviewResponse.getName());
        holder.txtDescription.setText(reviewResponse.getDecription());
    }

    @Override
    public int getItemCount() {
        return reviewResponseList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescription, txtName;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
