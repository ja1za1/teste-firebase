package com.firebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.R;
import com.firebase.models.Country;
import com.squareup.picasso.Picasso;


import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private List<Country> countryList;
    private final OnCountryClickListener listener;

    public interface OnCountryClickListener {
        void onCountryClick(Country country);
    }

    public CountryAdapter(List<Country> countryList, OnCountryClickListener listener) {
        this.countryList = countryList;
        this.listener = listener;
    }

    // Atualizar a lista de países
    public void setCountries(List<Country> newCountries) {
        this.countryList = newCountries;
        notifyDataSetChanged(); // Atualiza a lista
    }


    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.bind(country, listener);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvContinent;
        private final TextView tvComment;
        private final ImageView ivCountry;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvContinent = itemView.findViewById(R.id.tvContinent);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivCountry = itemView.findViewById(R.id.ivCountry);
        }

        public void bind(Country country, OnCountryClickListener listener) {
            tvName.setText(country.getName());
            tvContinent.setText(country.getContinent());
            tvComment.setText(country.getComment());
            Picasso.get().load(country.getImageUrl()).into(ivCountry);

            itemView.setOnClickListener(v -> listener.onCountryClick(country));
        }
    }
}
