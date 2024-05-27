package com.example.sit708_task_91p;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder> {

    // List of lost and found items
    private final List<LostFoundItem> items;
    // LayoutInflater to inflate the item layout
    private final LayoutInflater inflater;
    // Listener for item click events
    private final OnItemClickListener onItemClickListener;

    // Interface for handling item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor to initialize the adapter
    public LostFoundAdapter(Context context, List<LostFoundItem> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lost_found, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the views (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = items.get(position);
        holder.titleTextView.setText(item.getTitle());
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class to hold item views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);

            // Set the click listener for the item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    // Remove item from the list and notify the adapter
    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        saveItemsToSharedPreferences(inflater.getContext(), items);
    }

    // Save the updated list to SharedPreferences
    private void saveItemsToSharedPreferences(Context context, List<LostFoundItem> itemsList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LostFoundPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(itemsList);
        editor.putString("items", json);
        editor.apply();
    }
}
