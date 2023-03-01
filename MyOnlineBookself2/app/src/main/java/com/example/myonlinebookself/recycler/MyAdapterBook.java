package com.example.myonlinebookself.recycler;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myonlinebookself.MainActivity;
import com.example.myonlinebookself.items.Book;
import com.example.myonlinebookself.R;
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
    List<Book> item;

    public MyAdapterBook(Context context, List<Book> item) {
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
                FirebaseFirestore db = FirebaseFirestore.getInstance();                 //Getting instance of the database
                db.collection("OwnedBooks")
                        .document(holder.deleteButton.getTag().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.titleView.setText("Deleted book");
                                holder.authorView.setText("");
                                holder.imageView.setImageResource(context.getResources().getIdentifier("@drawable/book_deleted", null, context.getPackageName()));
                                holder.deleteButton.setVisibility(View.GONE);
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

    /**
     * Method to put the Id of a book in the tag of a button.
     *
     * @param holder is the holder
     * @param userId is the ID of the user that owns the book
     * @param bookId is the ID of the book that the user owns
     * */
    protected void setDocIdInTag(MyViewHolderBook holder, String userId, String bookId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();             //Getting instance of the database
        db.collection("OwnedBooks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){                                    //For every owned book
                        if (document.get("userId").equals(userId) && document.get("bookId").equals(bookId)){    //If the book belongs to the logged user
                            holder.deleteButton.setTag(document.getId());                                       //Setting the tag
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

