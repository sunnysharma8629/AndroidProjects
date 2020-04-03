package com.example.unittesting2.Models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "notes")
public class GetterSetter implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "Title")
    private String title;

    //@NonNull = this annotatin used when you want to variable ualue not be null.......
    @ColumnInfo(name = "Timestamp")
    private String timestamp;

    public GetterSetter(@NonNull String title, String timestamp) {
        this.title = title;
        this.timestamp = timestamp;
    }

    @Ignore
    public GetterSetter() {
    }
///// this constructor is used for making the fake database for unit testing into the local database..............
    @Ignore
    public GetterSetter (int id, @NonNull String title, String timestamp) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
    }

    //// this method is not Occupy the same location in the memory (Means) this method store the getter setter values
    // into different location;
    @Ignore
    public GetterSetter(GetterSetter getterSetter) {
        id = getterSetter.id;
        title=getterSetter.title;
        timestamp=getterSetter.timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GetterSetter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
       if (obj == null)
       {
           return false;
       }
       if (getClass()!=obj.getClass())
       {
           return false;
       }

       GetterSetter getterSetter =(GetterSetter)obj;
       return getterSetter.getId() == getId()&& getterSetter.getTitle().equals(getTitle())
               && getterSetter.getTimestamp().equals(getTimestamp());
    }


}
