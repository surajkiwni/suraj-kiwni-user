package com.kiwni.app.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.interfaces.FavoriteOnClickListener;
import com.kiwni.app.user.models.SqliteModelClass;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context context;
    List<SqliteModelClass> arrayListDb ;
    SqliteModelClass sqliteModelClass;
    FavoriteOnClickListener favoriteOnClickListener;

    public FavoriteAdapter(Context context, List<SqliteModelClass> arrayListDb, FavoriteOnClickListener favoriteOnClickListener) {
        this.context = context;
        this.arrayListDb = arrayListDb;
        this.favoriteOnClickListener = favoriteOnClickListener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_favorite,parent,false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {

        //array_list = array_list.get()

        sqliteModelClass = arrayListDb.get(position);

        holder.txtHome.setText(sqliteModelClass.getAddressType());
        holder.txtAddressFavorite.setText(sqliteModelClass.getAddress());

        if(holder.txtHome.getText().equals("Home")){
            holder.imgFavorites.setImageResource(R.drawable.home);
        }else if(holder.txtHome.getText().equals("Work")){
            holder.imgFavorites.setImageResource(R.drawable.work);
        }else{
            holder.imgFavorites.setImageResource(R.drawable.other);
        }


    }

    @Override
    public int getItemCount() {
        return arrayListDb.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView txtHome, txtAddressFavorite;
        ImageView imgFavorites;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHome = itemView.findViewById(R.id.txtHome);
            txtAddressFavorite = itemView.findViewById(R.id.txtAddressFavorite);
            imgFavorites = itemView.findViewById(R.id.imgHome);


            txtAddressFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

                    favoriteOnClickListener.onFavoriteOnClickListener(view,getAdapterPosition(),
                            txtHome.getText().toString(),txtAddressFavorite.getText().toString());


                }
            });
        }

    }
}
