package com.example.myonlinebookself;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterShop extends RecyclerView.Adapter<MyViewHolderShop> {

    Context context;
    List<Item> rentBook;

    public MyAdapterShop(Context context, List<Item> item) {
        this.context = context;
        this.rentBook = item;
    }

    @NonNull
    @Override
    public MyViewHolderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderShop(LayoutInflater.from(context).inflate(R.layout.market_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderShop holder, int position) {
        holder.titleView.setText(rentBook.get(holder.getAdapterPosition()).getTitle());
        holder.authorView.setText(rentBook.get(holder.getAdapterPosition()).getAuthor());
        holder.imageView.setImageResource(rentBook.get(holder.getAdapterPosition()).getImage());
        holder.descriptionView.setText(rentBook.get(holder.getAdapterPosition()).getDescription());

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailsBook.class);
                /*
                intent.putExtra("title", rentBook.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("author", rentBook.get(holder.getAdapterPosition()).getAuthor());
                intent.putExtra("image", rentBook.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("description", rentBook.get(holder.getAdapterPosition()).getDescription());
                */

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentBook.size();
    }
}
