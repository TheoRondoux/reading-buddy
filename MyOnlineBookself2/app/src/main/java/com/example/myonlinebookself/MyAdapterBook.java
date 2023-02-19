package com.example.myonlinebookself;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*La classe "MyAdapter" a deux variables membres, "context" et "item". "Context" représente le contexte de l'application
dans laquelle la vue défilante est affichée, tandis que "item" est une liste d'éléments qui seront affichés dans la vue défilante.

La classe "MyAdapter" implémente les méthodes de base nécessaires pour que la vue défilante
affiche les éléments de la liste. La méthode "onCreateViewHolder" crée une instance d'un "MyViewHolder"
pour chaque élément de la liste et associe la vue d'élément à un "layout" prédéfini dans le fichier XML.

La méthode "onBindViewHolder" est appelée pour chaque élément de la liste et utilise l'élément de la liste correspondant
 pour remplir les champs de la vue d'élément. La méthode "getItemCount" retourne le nombre total d'éléments dans la liste.
 */



public class MyAdapterBook extends RecyclerView.Adapter<MyViewHolderBook> {

    Context context;
    List<Item> item;

    public MyAdapterBook(Context context, List<Item> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolderBook(LayoutInflater.from(context).inflate(R.layout.mybook_view,parent,false));
}

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderBook holder, int position) {
        holder.titleView.setText(item.get(position).getTitle());
        holder.authorView.setText(item.get(position).getAuthor());
        holder.imageView.setImageResource(item.get(position).getImage());
    }


    @Override
    public int getItemCount() {
        return item.size();
    }
}

