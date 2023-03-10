package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.provider.FontRequest;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.FontsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myonlinebookself.items.Book;
import com.example.myonlinebookself.recycler.MyAdapterBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    List<Book> ownedBooks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {            //If title bar exists
            getSupportActionBar().hide();               //Hiding it
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.books);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading your books...");
        dialog.show();
        retrieveOwnedBooks();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.books:
                        return true;
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), RBShop.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), RBAccount.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    /**
     *  Method to add the owned books to the Recycle View
     *
     * @param db is the instance of the database
     * @param document is one of the results of a task
     * @param ownedBooks is the list of all the books owned by the logged user
     * */
    public void showOwnedBooks(FirebaseFirestore db, QueryDocumentSnapshot document, List<Book> ownedBooks){
        db.collection("Livre")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                        {

                            if (documentSnapshot.getId().equals(document.getString("bookId"))){
                                String uri = "@drawable/" + documentSnapshot.getString("image_name");
                                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                ownedBooks.add(new Book(documentSnapshot.getId(), documentSnapshot.getString("title"), documentSnapshot.getString("author"), documentSnapshot.getString("description"), documentSnapshot.getString("details"), imageResource));
                            }
                        }
                        buildRecycleView(ownedBooks);
                    }
                }
            });
    }

    public void buildRecycleView(List<Book> ownedBooks){

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new MyAdapterBook(getApplicationContext(),ownedBooks));
    }

    /**
     * Method that retrieve all the books owned by the logged user.
     *
     * */
    public void retrieveOwnedBooks(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();         //Getting an instance of the database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();                //Getting instance of the Firebase authentication system

        db.collection("OwnedBooks")                          //Looking into the "OwnedBooks" table
                .whereEqualTo("userId",mAuth.getUid())              //And searching all the books of the logged user
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0)                 //If the user has at least one book read, displays it
                        {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                ConstraintLayout constraintLayout = findViewById(R.id.noBookLayout);
                                constraintLayout.setVisibility(View.GONE);
                                showOwnedBooks(db, document, ownedBooks);
                                dialog.dismiss();
                            }
                        }
                        else if (task.isSuccessful() && task.getResult().size() == 0) {         //else, redirecting to the list of all books
                            RecyclerView recyclerView = findViewById(R.id.recycleview);
                            recyclerView.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                        else {
                            Log.d(TAG, "Error while looking for owned books");
                        }
                    }
                });

    }

    public List<Book> getOwnedBooks(){
        return this.ownedBooks;
    }
}

/*
Le code utilise la classe "RecyclerView" pour cr??er une vue d??filante qui affiche les ??l??ments de la liste.
D'abord, il r??cup??re une instance de la vue d??filante ?? partir de son identifiant de ressource, "recycleview",
?? l'aide de la m??thode "findViewById". Ensuite, il cr??e une nouvelle instance de la classe "LinearLayoutManager"
pour organiser les ??l??ments de la liste en une disposition de type liste verticale.

Enfin, il cr??e une instance de la classe "MyAdapter" pour fournir les donn??es d'??l??ments de la liste ?? la vue d??filante et
l'associe ?? la vue d??filante ?? l'aide de la m??thode "setAdapter". La m??thode "getApplicationContext()" est utilis??e
pour obtenir le contexte de l'application ?? partir de l'activit?? en cours.
 */