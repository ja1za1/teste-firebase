package com.firebase.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.adapters.CountryAdapter;
import com.firebase.models.Country;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import com.firebase.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvPlaceholder;
    private RecyclerView recyclerViewCountries;
    private CountryAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlaceholder = findViewById(R.id.tvPlaceholder);
        recyclerViewCountries = findViewById(R.id.recyclerViewCountries);
        ImageButton btnAddCountry = findViewById(R.id.btnAddCountry);
        ImageButton btnRefreshCountries = findViewById(R.id.btnRefreshCountries);

        recyclerViewCountries.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CountryAdapter(new ArrayList<>(), this::handleCountryClick);
        recyclerViewCountries.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadCountries();

        // Botão que chama a tela para adicionar país
        btnAddCountry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditCountryActivity.class);
            startActivity(intent);
        });

        btnRefreshCountries.setOnClickListener(v -> loadCountries());
    }


    // READ - Lê os arquivos do banco
    private void loadCountries() {
        db.collection("countries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Country> countries = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Country country = document.toObject(Country.class);
                        country.setId(document.getId()); // Configurar ID do documento Firestore
                        countries.add(country);
                    }
                    updateUI(countries);
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Erro ao carregar países", Toast.LENGTH_SHORT).show());
    }

    // Atualiza a lista com os país obtidos do banco
    private void updateUI(List<Country> countries) {
        if (countries.isEmpty()) {
            tvPlaceholder.setVisibility(View.VISIBLE);
            recyclerViewCountries.setVisibility(View.GONE);
        } else {
            tvPlaceholder.setVisibility(View.GONE);
            recyclerViewCountries.setVisibility(View.VISIBLE);
            adapter.setCountries(countries);
        }
    }

    // Função executada ao clicar em um país da lista
    private void handleCountryClick(Country country) {
        new AlertDialog.Builder(this)
                .setTitle(String.format("País %s", country.getName()))
                .setMessage("O que você deseja fazer com o país selecionado?")
                .setPositiveButton("Editar", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, AddEditCountryActivity.class);
                    intent.putExtra("country", country);
                    startActivity(intent);
                })
                .setNegativeButton("Deletar", (dialog, which) -> deleteCountry(country))
                .setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }


    // DELETE - Aqui estamos deletando um país que está no banco
    private void deleteCountry(Country country) {
        db.collection("countries")
                .document(country.getId())
                .delete()
                .addOnSuccessListener(e -> {
                    Toast.makeText(MainActivity.this, "País deletado com sucesso", Toast.LENGTH_SHORT).show();
                    loadCountries(); // Recarregar a lista após exclusão
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Erro ao deletar o país", Toast.LENGTH_SHORT).show();
                    loadCountries(); // Recarregar a lista após tentativa falha de deletar o país
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCountries();
    }
}
