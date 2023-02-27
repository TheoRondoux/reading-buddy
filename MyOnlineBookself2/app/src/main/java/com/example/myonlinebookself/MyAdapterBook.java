package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        holder.titleView.setText(item.get(position).getTitle());
        holder.authorView.setText(item.get(position).getAuthor());
        holder.imageView.setImageResource(item.get(position).getImage());
        setDocIdInTag(holder, mAuth.getUid(), item.get(position).getId());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("OwnedBooks")
                        .document(holder.deleteButton.getTag().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Book successfuly deleted!", Toast.LENGTH_LONG).show();
                                //context.startActivity(new Intent(context, MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });
    }

    protected void setDocIdInTag(MyViewHolderBook holder, String userId, String bookId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OwnedBooks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.get("userId").equals(userId) && document.get("bookId").equals(bookId)){
                            holder.deleteButton.setTag(document.getId());
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}

