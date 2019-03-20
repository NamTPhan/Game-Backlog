package com.npdevelopment.gamebacklog.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.npdevelopment.gamebacklog.model.Game;

import java.util.List;

@Dao
public interface GameBacklogDao {

    @Insert
    void insert(Game game);

    @Insert
    void insert(List<Game> games);

    @Delete
    void delete(Game game);

    @Delete
    void delete(List<Game> games);

    @Update
    void update(Game game);

    @Query("SELECT * from gamebacklog_table")
    LiveData<List<Game>> getAllGames();
}
