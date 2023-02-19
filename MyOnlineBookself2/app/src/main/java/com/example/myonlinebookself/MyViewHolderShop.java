package com.example.myonlinebookself;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderShop extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleView, authorView, descriptionView;
    Button detailsButton;


    public MyViewHolderShop(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageBookViewShop);
        titleView = itemView.findViewById(R.id.titleTextBookViewShop);
        authorView = itemView.findViewById(R.id.authorBookViewShop);
        descriptionView = itemView.findViewById(R.id.descriptionBook);
        detailsButton = itemView.findViewById(R.id.whiteBookButton);
    }
}
