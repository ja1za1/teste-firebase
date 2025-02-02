package com.firebase.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.R;
import com.firebase.models.Country;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class AddEditCountryActivity extends AppCompatActivity {

    private Spinner spinnerCountry, spinnerContinent;
    private EditText etImageUrl, etComment;

    private FirebaseFirestore db;
    private Country country; // Objeto Country usado para edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_country);

        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerContinent = findViewById(R.id.spinnerContinent);
        etImageUrl = findViewById(R.id.etImageUrl);
        etComment = findViewById(R.id.etComment);
        Button btnSave = findViewById(R.id.btnSave);

        // Obtendo a instância do banco de dados
        db = FirebaseFirestore.getInstance();

        loadCountriesIntoSpinner();
        loadContinentsIntoSpinner();

        country = (Country) getIntent().getSerializableExtra("country");
        if (country != null) {
            populateFields(country);
        }

        btnSave.setOnClickListener(v -> saveCountry());
    }

    private void populateFields(Country country) {
        Log.d("PAIS", country.toString());
        selectContinentInSpinner(country.getContinent());
        selectCountryInSpinner(country.getName());
        etImageUrl.setText(country.getImageUrl());
        etComment.setText(country.getComment());
    }

    private void saveCountry() {
        String name = spinnerCountry.getSelectedItem().toString();
        String continent = spinnerContinent.getSelectedItem().toString();
        String imageUrl = etImageUrl.getText().toString().trim();
        String comment = etComment.getText().toString().trim();

        if(spinnerContinent.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Por favor, selecione um continente", Toast.LENGTH_SHORT).show();
            return;
        }

        if(spinnerCountry.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Por favor, selecione um país", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrl.isBlank()) {
            Toast.makeText(this, "Por favor, forneça uma URL de uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        // CREATE - Aqui estamos adicionando um novo país ao banco
        if (country == null) {
            Country newCountry = new Country(name, continent, imageUrl, comment);
            db.collection("countries")
                    .add(newCountry)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "País adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Retornar à lista
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao adicionar país", Toast.LENGTH_SHORT).show());
        }
        // UPDATE - Aqui estamos atualizando um país que está no banco
        else {
            country.setName(name);
            country.setContinent(continent);
            country.setImageUrl(imageUrl);
            country.setComment(comment);

            // Adicionar aqui a função de editar do banco
            db.collection("countries").document(country.getId())
                    .set(country)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "País atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish(); // Retornar à lista
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao atualizar país", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadCountriesIntoSpinner() {
        List<String> countries = Arrays.asList(
                "Selecione um país",
                "Afeganistão", "África do Sul", "Alemanha", "Brasil", "Canadá", "China",
                "Estados Unidos", "França", "Índia", "Japão", "México", "Reino Unido", "Rússia", "Tailândia", "Austrália");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setSelection(0);
    }

    private void loadContinentsIntoSpinner() {
        List<String> continents = Arrays.asList(
                "Selecione um continente",
                "América", "África", "Europa", "Ásia", "Oceania");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, continents);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContinent.setAdapter(adapter);

        spinnerCountry.setSelection(0);
    }

    private void selectCountryInSpinner(String countryName) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCountry.getAdapter();
        int position = adapter.getPosition(countryName);
        if (position != -1) {
            spinnerCountry.setSelection(position);
        }
    }

    private void selectContinentInSpinner(String continent) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerContinent.getAdapter();
        int position = adapter.getPosition(continent);
        if (position != -1) {
            spinnerContinent.setSelection(position);
        }
    }
}
