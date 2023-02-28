package com.example.myonlinebookself.recycler;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlinebookself.R;

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
