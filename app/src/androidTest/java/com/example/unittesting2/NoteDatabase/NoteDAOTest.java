package com.example.unittesting2.NoteDatabase;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.unittesting2.DAOPackage.DaoInterface;
import com.example.unittesting2.Models.GetterSetter;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import unittesting2.LivedataTestingClass;
import unittesting2.TestiingValues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NoteDAOTest extends NotedatabaseTest {

//////////////Insrumentation tests/////////////////////////////////
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

   @Test
    public void insertMockTypedata() throws Exception
   {
       GetterSetter getterSetter1 = new GetterSetter(TestiingValues.TEST_NOTE_1);

       //Insert
       getNoteDao().insertnote(getterSetter1);

       //Read
       LivedataTestingClass<List<GetterSetter>> liveDataTestUtil = new LivedataTestingClass<>();
       List<GetterSetter> insertedNotes = liveDataTestUtil.getValue(getNoteDao().GetNotes());

       //Assertion
       assertNotNull(insertedNotes);

       assertEquals(getterSetter1.getTimestamp(), insertedNotes.get(0).getTimestamp());
       assertEquals(getterSetter1.getTitle(), insertedNotes.get(0).getTitle());

       getterSetter1.setId(insertedNotes.get(0).getId());
       assertEquals(getterSetter1, insertedNotes.get(0));

       // delete
       getNoteDao().deletenote(getterSetter1);

       // confirm the database is empty
       insertedNotes = liveDataTestUtil.getValue(getNoteDao().GetNotes());
       assertEquals(0, insertedNotes.size());

   }

    /*
     Insert, Read, Update, Read, Delete,
  */
   //@Test
   //public void insertReadUpdateReadDelete() throws Exception{

       // Note note = new Note(TestUtil.TEST_NOTE_1);

        // insert
      //  getNoteDao().insertNote(note).blockingGet(); // wait until inserted

        // read
      //  LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
       // List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());

       // assertNotNull(insertedNotes);

       // assertEquals(note.getContent(), insertedNotes.get(0).getContent());
      //  assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());
      //  assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());

      //  note.setId(insertedNotes.get(0).getId());
      //  assertEquals(note, insertedNotes.get(0));

        // update
     //   note.setTitle(TEST_TITLE);
       // note.setContent(TEST_CONTENT);
      //  note.setTimestamp(TEST_TIMESTAMP);
      //  getNoteDao().updateNote(note).blockingGet();

        // read
      //  insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());

     //   assertEquals(TEST_TITLE, insertedNotes.get(0).getTitle());
     //   assertEquals(TEST_CONTENT, insertedNotes.get(0).getContent());
     //   assertEquals(TEST_TIMESTAMP, insertedNotes.get(0).getTimestamp());

      //  note.setId(insertedNotes.get(0).getId());
      //  assertEquals(note, insertedNotes.get(0));

        // delete
      //  getNoteDao().deleteNote(note).blockingGet();

        // confirm the database is empty
       // insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
      //  assertEquals(0, insertedNotes.size());
    //}



    /*
        Insert note with null title, throw exception
     */
   // @Test(expected = SQLiteConstraintException.class)
   // public void insert_nullTitle_throwSQLiteConstraintException() throws Exception{

        //final Note note = new Note(TestUtil.TEST_NOTE_1);
       // note.setTitle(null);

        // insert
       // getNoteDao().insertNote(note).blockingGet();
    //}


    /*
        Insert, Update with null title, throw exception
     */

  //  @Test(expected = SQLiteConstraintException.class)
    //public void updateNote_nullTitle_throwSQLiteConstraintException() throws Exception{

       // Note note = new Note(TestUtil.TEST_NOTE_1);

        // insert
       // getNoteDao().insertNote(note).blockingGet();

       // // read
      //  LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
      //  List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
      //  assertNotNull(insertedNotes);

        // update
      //  note = new Note(insertedNotes.get(0));
      //  note.setTitle(null);
       // getNoteDao().updateNote(note).blockingGet();

   // }

}
