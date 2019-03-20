package com.npdevelopment.gamebacklog.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.npdevelopment.gamebacklog.R;
import com.npdevelopment.gamebacklog.model.Game;

import java.util.List;

public class GameBacklogAdapter extends RecyclerView.Adapter<GameViewHolder> {

    public List<Game> gameObjectList;

    public GameBacklogAdapter(List<Game> gameObjectList) {
        this.gameObjectList = gameObjectList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_game_cell, viewGroup, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        // Gets a single item in the list from its position
        final Game gameObject = gameObjectList.get(position);

        holder.gameTitle.setText(gameObject.getTitle());
        holder.gamePlatform.setText(gameObject.getPlatform());
        holder.gameStatus.setText(gameObject.getStatus());
        holder.gameDate.setText("Created: " + gameObject.getDate());
        holder.gameDateEdited.setText("Edited: " + gameObject.getDate_edited());
    }

    /**
     * Method for refreshing the data
     * @param newList the list that has to be refreshed
     */
    public void refreshList(List<Game> newList) {
        gameObjectList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return gameObjectList.size();
    }
}
