package com.example.gapsitest.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gapsitest.R;
import com.example.gapsitest.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products = new ArrayList<>();
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void addProducts(List<Product> newProducts) {
        int startPosition = products.size();
        products.addAll(newProducts);
        notifyItemRangeInserted(startPosition, newProducts.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.titleTextView.setText(product.getTitle());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));

        // Cargar imagen
        Glide.with(context)
                .load(product.getThumbnail())
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView priceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}