package com.npdevelopment.gamebacklog.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.npdevelopment.gamebacklog.database.GameRepository;
import com.npdevelopment.gamebacklog.model.Game;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private GameRepository mGameRepository;
    private LiveData<List<Game>> mGames;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mGameRepository = new GameRepository(application.getApplicationContext());
        mGames = mGameRepository.getAllGames();
    }

    public LiveData<List<Game>> getGames() {
        return mGames;
    }

    public void insert(Game game) {
        mGameRepository.insert(game);
    }

    public void insertAll(List<Game> games) {
        mGameRepository.insertAll(games);
    }

    public void update(Game game) {
        mGameRepository.update(game);
    }

    public void delete(Game game) {
        mGameRepository.delete(game);
    }

    public void deleteAllGames(List<Game> games) {
        mGameRepository.deleteAll(games);
    }
}
