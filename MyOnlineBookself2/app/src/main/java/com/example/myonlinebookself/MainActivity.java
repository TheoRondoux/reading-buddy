package com.example.myonlinebookself;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.books);


        List<Item> items = new ArrayList<>();
        items.add(new Item("Harry Potter 1","J.K Rowling","Harry Potter à l école des sorciers.","qczcz",R.drawable.harry_1));
        items.add(new Item("Harry Potter 2","J.K Rowling","Harry Potter et la chambre des secrets.","csczsczc",R.drawable.harry_2));
        items.add(new Item("Harry Potter 3","J.K Rowling","Harry Potter et le prisonnier d'Azkaban.","qczczzecze",R.drawable.harry_3));
        items.add(new Item("Harry Potter 4","J.K Rowling","Harry Potter et la coupe de feu.","czzcz",R.drawable.harry_4));
        items.add(new Item("Harry Potter 5","J.K Rowling","Harry Potter et l'Ordre du Phénix.","dczczcz",R.drawable.harry_5));
        items.add(new Item("Harry Potter 6","J.K Rowling","Harry Potter et le Prince de sang-mêlé.","zczczcz",R.drawable.harry_6));
        items.add(new Item("Harry Potter 7","J.K Rowling","Harry Potter et les Reliques de la Mort.","czczcz",R.drawable.harry_7));


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

        /*
        Le code utilise la classe "RecyclerView" pour créer une vue défilante qui affiche les éléments de la liste.
        D'abord, il récupère une instance de la vue défilante à partir de son identifiant de ressource, "recycleview",
        à l'aide de la méthode "findViewById". Ensuite, il crée une nouvelle instance de la classe "LinearLayoutManager"
        pour organiser les éléments de la liste en une disposition de type liste verticale.

        Enfin, il crée une instance de la classe "MyAdapter" pour fournir les données d'éléments de la liste à la vue défilante et
        l'associe à la vue défilante à l'aide de la méthode "setAdapter". La méthode "getApplicationContext()" est utilisée
        pour obtenir le contexte de l'application à partir de l'activité en cours.
         */

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapterBook(getApplicationContext(),items));
    }

    public void logoutClick(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}