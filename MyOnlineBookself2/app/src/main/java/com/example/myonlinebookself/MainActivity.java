package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.books);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading your books...");
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        List<Item> ownedBooks = new ArrayList<>();

        db.collection("OwnedBooks")
            .whereEqualTo("userId",mAuth.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && task.getResult().size() > 0)
                    {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            generatesBook(db, document, ownedBooks, bottomNavigationView);
                        }
                    }
                    else if (task.isSuccessful() && task.getResult().size() == 0) {
                        Toast.makeText(MainActivity.this, "No books in you library...", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, Shop.class));
                    }
                    else {
                        Log.d(TAG, "Error while looking for owned books");
                    }
                }
            });

        dialog.dismiss();
    }

    public void generatesBook(FirebaseFirestore db, QueryDocumentSnapshot document, List<Item> ownedBooks, BottomNavigationView bottomNavigationView){
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
                                ownedBooks.add(new Item(documentSnapshot.getId(), documentSnapshot.getString("title"), documentSnapshot.getString("author"), documentSnapshot.getString("description"), documentSnapshot.getString("details"), imageResource));
                            }
                        }
                        buildRecycleView(bottomNavigationView, ownedBooks);
                    }
                }
            });
    }

    public void buildRecycleView(BottomNavigationView bottomNavigationView, List<Item> ownedBooks){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(),Shop.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.books:
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(),Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new MyAdapterBook(getApplicationContext(),ownedBooks));
    }
}

                        /*
                        Le code utilise la classe "RecyclerView" pour créer une vue défilante qui affiche les éléments de la liste.
                        D'abord, il récupère une instance de la vue défilante à partir de son identifiant de ressource, "recycleview",
                        à l'aide de la méthode "findViewById". Ensuite, il crée une nouvelle instance de la classe "LinearLayoutManager"
                        pour organiser les éléments de la liste en une disposition de type liste verticale.

                        Enfin, il crée une instance de la classe "MyAdapter" pour fournir les données d'éléments de la liste à la vue défilante et
                        l'associe à la vue défilante à l'aide de la méthode "setAdapter". La méthode "getApplicationContext()" est utilisée
                        pour obtenir le contexte de l'application à partir de l'activité en cours.
                         */