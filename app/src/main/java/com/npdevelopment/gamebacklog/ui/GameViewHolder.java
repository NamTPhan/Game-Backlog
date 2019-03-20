package com.npdevelopment.gamebacklog.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.npdevelopment.gamebacklog.R;

public class GameViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView gameTitle, gamePlatform, gameStatus, gameDate, gameDateEdited;
    public CardView gameCard;

    public GameViewHolder(View itemView) {
        super(itemView);
        gameCard = itemView.findViewById(R.id.gameCard);
        gameTitle = itemView.findViewById(R.id.tv_game_title);
        gamePlatform = itemView.findViewById(R.id.tv_game_platform);
        gameStatus = itemView.findViewById(R.id.tv_game_status);
        gameDate = itemView.findViewById(R.id.tv_game_date);
        gameDateEdited = itemView.findViewById(R.id.tv_game_date_edited);
        view = itemView;
    }
}
