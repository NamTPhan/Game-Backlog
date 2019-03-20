package com.npdevelopment.gamebacklog.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.npdevelopment.gamebacklog.model.Game;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {

    private GameBacklogRoomDatabase mAppDatabase;
    private GameBacklogDao mGameBacklogDao;
    private LiveData<List<Game>> mGames;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameRepository(Context context) {
        mAppDatabase = GameBacklogRoomDatabase.getDatabase(context);
        mGameBacklogDao = mAppDatabase.gameBacklogDao();
        mGames = mGameBacklogDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return mGames;
    }

    public void insert(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.insert(game);
            }
        });
    }

    public void insertAll(final List<Game> games) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.insert(games);
            }
        });
    }

    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.update(game);
            }
        });
    }

    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.delete(game);
            }
        });
    }

    public void deleteAll(final List<Game> games) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.delete(games);
            }
        });
    }
}
