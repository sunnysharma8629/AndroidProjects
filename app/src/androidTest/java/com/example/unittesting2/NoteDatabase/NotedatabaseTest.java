package com.example.unittesting2.NoteDatabase;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.unittesting2.DAOPackage.DaoInterface;
//import com.example.unittesting2.DAOPackage.MockNoteDatabaseTesting;

import org.junit.After;
import org.junit.Before;

public abstract class NotedatabaseTest {

    // system under test
    private MockNoteDatabaseTesting noteDatabase;


    public DaoInterface getNoteDao(){
        return noteDatabase.getDAOObjects();
    }

    @Before
    public void init(){
        noteDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                MockNoteDatabaseTesting.class
        ).build();
    }

    @After
    public void finish(){
        noteDatabase.close();
    }

}
