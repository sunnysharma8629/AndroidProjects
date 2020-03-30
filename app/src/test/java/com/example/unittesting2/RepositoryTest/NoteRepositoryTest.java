package com.example.unittesting2.RepositoryTest;

import androidx.lifecycle.MutableLiveData;

import com.example.unittesting2.DAOPackage.DaoInterface;
import com.example.unittesting2.DAOPackage.Repository;
import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;
import com.example.unittesting2.Util.InstantExecutorInstanceRuleJunit5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import unittesting2.LivedataTestingClass;
import unittesting2.TestiingValues;

import static com.example.unittesting2.DAOPackage.Repository.DELETE_FAILURE;
import static com.example.unittesting2.DAOPackage.Repository.DELETE_SUCCESS;
import static com.example.unittesting2.DAOPackage.Repository.INVALID_NOTE_ID;
import static com.example.unittesting2.DAOPackage.Repository.Insert_Failure;
import static com.example.unittesting2.DAOPackage.Repository.Insert_Sucess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//This Annotiation used only with BeforeAll Annotiation....

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorInstanceRuleJunit5.class)
public class NoteRepositoryTest  {

    private Repository NoteRepository;
    ///@Mock //************* This Annotation used only for intialization the class purpose...
     private DaoInterface NoteDAO;
   // private NoteDatabaseCreateClass noteDatabaseCreateClass;
    private Context c1;
    private static final GetterSetter TEST_NOTE_1 = new GetterSetter
            (1,"Take out the trash", "It's garbage day tomorrow.");

    public NoteRepositoryTest(Context c1) {
        this.c1 = c1;
    }

    @BeforeEach
    public void EachTest()
    {
       // MockitoAnnotations.initMocks(this);
        NoteDAO = mock(DaoInterface.class);
        NoteRepository = new Repository((android.content.Context) c1);
    }

    //// this annotation used when you are making one time instance of all class and then you are making all test
    //  and all test using same instance.........//////////////////


  //  @BeforeAll
   // public void EachTest()
  //  {
        // MockitoAnnotations.initMocks(this);
      //  NoteDAO = Mockito.mock(DaoInterface.class);
      //  NoteRepository = new Repository(NoteDAO);

   // }

    @Test
    void insertNote()throws Exception
    {

        final Long insertedRows = 1L;
        final Single<Long> returndata = Single.just(insertedRows);
        when(NoteDAO.insertnote(any(GetterSetter.class))).thenReturn(returndata);

        final Resource<Integer> returnedValue = NoteRepository.insertdata(TEST_NOTE_1).blockingFirst();
        verify(NoteDAO).insertnote(any(GetterSetter.class));
        System.out.println("returned value :"+returnedValue.data);
        assertEquals(Resource.success(1,Insert_Sucess),returnedValue);

    }

    @Test
    void InsertNoteError_Return()throws Exception
    {
        /////these UpperLines check one row inserted or not in database..../////////
        //1)if  Row Inserted then set data in inserted row/////
        final Long insertedRows = -1L;
        final Single<Long> returndata = Single.just(insertedRows);
        when(NoteDAO.insertnote(any(GetterSetter.class))).thenReturn(returndata);

        final Resource<Integer> returnedValue = NoteRepository.insertdata(TEST_NOTE_1).blockingFirst();

        verify(NoteDAO).insertnote(any(GetterSetter.class));
        System.out.println("returned value :"+returnedValue.data);
        assertEquals(Resource.error(null,Insert_Failure),returnedValue);

    }

    @Test
    void insertNote_nullTitle()throws Exception {
      Exception exception = assertThrows(Exception.class, new Executable() {
         @Override
         public void execute() throws Throwable {
             final GetterSetter getterSetter2 = new GetterSetter(TEST_NOTE_1);
             getterSetter2.setTitle(null);
             NoteRepository.insertdata(getterSetter2);

         }
     });
       System.out.println("ExceptionMessage :"+exception.getMessage());
      assertEquals("Title has been null",exception.getMessage());

    }

    @Test
    void UpdateNote_Return()throws Exception
    {
        //Arrange
        final int updatenote =1;
        when(NoteDAO.updatenote(any(GetterSetter.class))).thenReturn(Single.just(updatenote));

        //Act
        final Resource<Integer> returnedValue = NoteRepository.UpdateData(TEST_NOTE_1).blockingFirst();

        //Assert
        verify(NoteDAO).updatenote(any(GetterSetter.class));
        assertEquals(Resource.success(updatenote,"Update_Data_Inserted"),returnedValue);
    }



    @Test
    void UpdateNote_Return_Failure()throws Exception
    {

        ////this method inserted row into database through when(NoteDao.updatenote) method
        //Arrange
        final int updatefailed = -1;
        when(NoteDAO.updatenote(any(GetterSetter.class))).thenReturn(Single.just(updatefailed));

        /////Provide data through this method?/////
        //Act
        final Resource<Integer> returnedValue = NoteRepository.UpdateData(TEST_NOTE_1).blockingFirst();

        //Assert
        verify(NoteDAO).updatenote(any(GetterSetter.class));
        assertEquals(Resource.error(null,"Not_updated"),returnedValue);
    }

 /*
        delete note
        null id
        throw exception
     */

    @Test
    void deleteNote_nullId_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final GetterSetter note = new GetterSetter(TEST_NOTE_1);
                note.setId(-1);
                NoteRepository.deleteNote(note);
            }
        });

        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }

    /*
        delete note
        delete success
        return Resource.success with deleted row
     */


    //////ye method tab use hote hai jab operation ke bad data ko abserve karna hota hai..
    // Examples = deletenote,insertnote methods......
    @Test
    void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        // Arrange
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        LivedataTestingClass<Resource<Integer>> liveDataTestUtil = new LivedataTestingClass<>();
        when(NoteDAO.deletenote(any(GetterSetter.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(NoteRepository.deleteNote(TEST_NOTE_1));

        // Assert
        assertEquals(successResponse, observedResponse);
    }


    /*
        delete note
        delete failure
        return Resource.error
     */
    @Test
    void deleteNote_deleteFailure_returnResourceError() throws Exception {
        // Arrange
        final int deletedRow = -1;
        Resource<Integer> errorResponse = Resource.error(null, DELETE_FAILURE);
        LivedataTestingClass<Resource<Integer>> liveDataTestUtil = new LivedataTestingClass<>();
        when(NoteDAO.deletenote(any(GetterSetter.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(NoteRepository.deleteNote(TEST_NOTE_1));

        // Assert
        assertEquals(errorResponse, observedResponse);
    }


    /*
        retrieve notes
        return list of notes
     */

    @Test
    void getNotes_returnListWithNotes() throws Exception {
        // Arrange
        List<GetterSetter> notes = TestiingValues.TEST_NOTES_LIST;
        LivedataTestingClass<List<GetterSetter>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<List<GetterSetter>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(NoteDAO.GetNotes()).thenReturn(returnedData);

        // Act
        List<GetterSetter> observedData = liveDataTestUtil.getValue(NoteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

    /*
        retrieve notes
        return empty list
     */

    @Test
    void getNotes_returnEmptyList() throws Exception {
        // Arrange
        List<GetterSetter> notes = new ArrayList<>();
        LivedataTestingClass<List<GetterSetter>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<List<GetterSetter>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(NoteDAO.GetNotes()).thenReturn(returnedData);

        // Act
        List<GetterSetter> observedData = liveDataTestUtil.getValue(NoteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

}
