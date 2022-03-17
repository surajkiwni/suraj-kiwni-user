package com.kiwni.app.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.datamodels.BookingModel;
import com.kiwni.app.user.datamodels.DataModels;
import com.kiwni.app.user.interfaces.BookingListItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TitleItemAdapter extends RecyclerView.Adapter<TitleItemAdapter.TitleViewHolder> {

    private List<DataModels> mList;
    private List<BookingModel> list = new ArrayList<>();
    BookingListItemClickListener listener;

    public TitleItemAdapter(List<DataModels> mList,BookingListItemClickListener listener){
        this.mList  = mList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public TitleItemAdapter.TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_each_item,parent,false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleItemAdapter.TitleViewHolder holder, int position) {

        DataModels model = mList.get(position);
        holder.mTextView.setText(model.getItemText());

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);


        NestedAdapter adapter = new NestedAdapter(list,listener);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.constraintLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setExpandable(!model.isExpandable());
                list = model.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout constraintLayout1,expandableLayout;
        private TextView mTextView;
        private ImageView mArrowImage;
        private RecyclerView nestedRecyclerView;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout1 = itemView.findViewById(R.id.constraintLayout1);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mTextView = itemView.findViewById(R.id.carNameText);
            //mArrowImage = itemView.findViewById(R.id.arro_imageview);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);
        }
    }
}
