package com.example.unittesting2.DAOPackage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.unittesting2.Models.GetterSetter;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DaoInterface {

    @Insert
    Single<Long> insertnote(GetterSetter getterSetters)throws Exception;

    @Query("SELECT * FROM  notes")
    LiveData<List<GetterSetter>> GetNotes();

    @Update
    Single<Integer> updatenote(GetterSetter...getterSetters)throws Exception;

    @Delete
    Single<Integer> deletenote(GetterSetter...getterSetters)throws Exception;
}
