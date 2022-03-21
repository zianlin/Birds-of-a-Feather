package com.example.birdsofafeather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao {
    @Query("SELECT COUNT(*) > 0 FROM favorites WHERE er_id = :erID AND ed_id = :edID")
    boolean checkFavorited(String erID, String edID);

    @Query("SELECT ed_id FROM favorites")
    List<String> getAll();

    @Insert
    void insert(Favorites favorite);

    @Query("DELETE FROM favorites WHERE er_id = :erID AND ed_id = :edID")
    void delete(String erID, String edID);

}
