package com.example.unittesting2.DAOPackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.unittesting2.Models.GetterSetter;

import io.reactivex.annotations.NonNull;

@Database(entities = {GetterSetter.class},version = 2)

public abstract class NoteDatabaseCreateClass extends RoomDatabase {

    public static final String DATABASE_NAME = "NotekeepingDatabase";

    public static NoteDatabaseCreateClass noteDatabaseCreateClass;

    static NoteDatabaseCreateClass getInstance(final Context context)
    {
        if (noteDatabaseCreateClass ==null)
        {
            noteDatabaseCreateClass = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDatabaseCreateClass.class,DATABASE_NAME).build();
        }
        return noteDatabaseCreateClass;
    }

    @NonNull
    public abstract DaoInterface getDAOObjects();

}
