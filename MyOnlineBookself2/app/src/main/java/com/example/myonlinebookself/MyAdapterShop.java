package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.titleView.setText(rentBook.get(holder.getAdapterPosition()).getTitle());
        holder.authorView.setText(rentBook.get(holder.getAdapterPosition()).getAuthor());
        holder.imageView.setImageResource(rentBook.get(holder.getAdapterPosition()).getImage());
        holder.descriptionView.setText(rentBook.get(holder.getAdapterPosition()).getDescription());
        holder.detailsButton.setTag(rentBook.get(holder.getAdapterPosition()).getId());

        db.collection("OwnedBooks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean hasAlreadyOwnedThisBook = false;
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if (document.getString("userId").equals(mAuth.getUid()) && document.getString("bookId").equals(holder.detailsButton.getTag()))
                                {
                                    holder.detailsButton.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRead(holder.detailsButton.getTag().toString(), mAuth.getUid(), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentBook.size();
    }

    public void onClickRead(String bookId, String userId, MyViewHolderShop holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OwnedBooks")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean hasAlreadyOwnedThisBook = false;
                    if (task.isSuccessful()){

                        for (QueryDocumentSnapshot document : task.getResult()){
                            if (document.getString("userId").equals(userId) && document.getString("bookId").equals(bookId))
                            {
                                hasAlreadyOwnedThisBook = true;
                            }
                        }
                    }

                    if (task.isSuccessful() && !hasAlreadyOwnedThisBook){
                        addBookToCollection(bookId, userId, holder);
                        holder.detailsButton.setWidth(0);
                    }
                }
            });

    }

    public void addBookToCollection(String bookId, String userId, MyViewHolderShop holder){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> ownedBook = new HashMap<>();
        ownedBook.put("bookId", bookId);
        ownedBook.put("userId", userId);
        db.collection("OwnedBooks")
                .add(ownedBook)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Book successfuly added to your bookshelf", Toast.LENGTH_SHORT).show();
                        holder.detailsButton.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Error while adding the book: ", e);
                    }
                });
    }
}
