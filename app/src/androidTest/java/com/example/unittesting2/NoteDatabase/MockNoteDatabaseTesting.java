package com.example.unittesting2.NoteDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.unittesting2.DAOPackage.DaoInterface;
import com.example.unittesting2.Models.GetterSetter;

@Database(entities = {GetterSetter.class},version = 1)
public abstract class MockNoteDatabaseTesting extends RoomDatabase {

    public static final String DATABASE_NAME = "NotekeepingDatabase";

    public abstract DaoInterface getDAOObjects();

}
