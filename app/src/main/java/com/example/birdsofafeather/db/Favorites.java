package com.example.birdsofafeather.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorites {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "er_id")
    private String erId;

    @ColumnInfo(name = "ed_id")
    private String edId;

    public Favorites(String erId, String edId){
        this.edId = edId;
        this.erId = erId;
    }

    public String getErId(){
        return this.erId;
    }

    public String getEdId(){
        return this.edId;
    }

    public int getId() {return this.id;}

    public void setErId(String erId){
        this.erId = erId;
    }

    public void setEdId(String edId){
        this.edId = edId;
    }

    public void setId(int id){
        this.id = id;
    }
}
