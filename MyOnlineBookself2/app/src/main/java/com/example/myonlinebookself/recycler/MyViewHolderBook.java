package com.example.myonlinebookself.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlinebookself.R;


/*La classe "MyViewHolder" a quatres variables membres.

Le constructeur de la classe "MyViewHolder" reçoit une instance de la vue d'élément de la liste,
 représentée par l'objet "itemView". Dans le constructeur, les vues d'élément sont initialisées en utilisant les identifiants
 définis dans le fichier XML.

Une fois initialisées, ces vues sont stockées dans les variables membres correspondantes de la classe "MyViewHolder",
 ce qui permet d'y accéder facilement dans la méthode "onBindViewHolder" de la classe "MyAdapter".

 */
public class MyViewHolderBook extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleView, authorView;
    Button deleteButton;


    public MyViewHolderBook(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageBookViewShop);
        titleView = itemView.findViewById(R.id.titleTextBookViewShop);
        authorView = itemView.findViewById(R.id.authorBookViewShop);
        deleteButton = itemView.findViewById(R.id.DeleteBookButton);
    }
}


