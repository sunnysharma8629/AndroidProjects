package com.example.unittesting2.ViewModelTest;

import androidx.lifecycle.MutableLiveData;

import com.example.unittesting2.DAOPackage.DataViewModel;
import com.example.unittesting2.DAOPackage.NotelistViewModel;
import com.example.unittesting2.DAOPackage.Repository;
import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;
import com.example.unittesting2.Util.InstantExecutorInstanceRuleJunit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;
import unittesting2.LivedataTestingClass;
import unittesting2.TestiingValues;

import static com.example.unittesting2.DAOPackage.DataViewModel.NO_CONTENT_ERROR;
import static com.example.unittesting2.DAOPackage.Repository.DELETE_FAILURE;
import static com.example.unittesting2.DAOPackage.Repository.DELETE_SUCCESS;
import static io.reactivex.Observable.never;
import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static unittesting2.TestiingValues.TEST_NOTE_1;

@ExtendWith(InstantExecutorInstanceRuleJunit5.class)
public class ViewmodelTest {


    public static final String Insert_Sucess="data-inserted";
    public static final String Insert_Failure="data-inserted2";
    public static final String Update_Data ="Data will be updated";
    public static final String No_Content_Error ="no error in data";
    private DataViewModel viewmodel;
    private NotelistViewModel notelistViewModel;

    @Mock
    private Repository Noterepositoyr;

    @BeforeEach
    public void init()
    {
      MockitoAnnotations.initMocks(this);
      viewmodel = new DataViewModel(Noterepositoyr);
      notelistViewModel= new NotelistViewModel(Noterepositoyr);
    }

    /*
        can't observe a note that hasn't been set
     */

    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {

        // Arrange
        LivedataTestingClass<GetterSetter> liveDataTestingclass = new LivedataTestingClass<>();
        // Act
        GetterSetter getterSetter = liveDataTestingclass.getValue(viewmodel.observenote());
        // Assert
        Assertions.assertNull(getterSetter);
    }

    /*
        Observe a note has been set and onChanged will trigger in activity
     */

    @Test
    void observeNote_whenSet() throws Exception {

        // Arrange
        GetterSetter getterSetter1= new GetterSetter(TEST_NOTE_1);
        LivedataTestingClass<GetterSetter> liveDataTestUtil = new LivedataTestingClass<>();

        // Act
        viewmodel.setNote(getterSetter1);
        GetterSetter observedNote = liveDataTestUtil.getValue(viewmodel.observenote());

        // Assert
        assertEquals(getterSetter1, observedNote);
    }

      /*
         Insert a new note and observe row returned
      */

    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange//
        GetterSetter getterSetter2 = new GetterSetter(TEST_NOTE_1);
        LivedataTestingClass<Resource<Integer>> liveDataTestingclass = new LivedataTestingClass<>();
        final int insertedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(insertedRow,Insert_Sucess));
        when(Noterepositoyr.insertdata((GetterSetter)ArgumentMatchers.any(GetterSetter.class))).thenReturn(returnedData);
        // Act//
        viewmodel.setNote(getterSetter2);
        viewmodel.setIsNewNote(true);
        // viewmodel.setIsNewNote(true);//
        Resource<Integer> returnedValue = liveDataTestingclass.getValue(viewmodel.saveNote());
        System.out.println("Returned value: "+returnedValue);
        // Assert//
        assertEquals(Resource.success(insertedRow,Insert_Sucess), returnedValue);

    }

     /*
        insert: dont return a new row without observer
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {

        // Arrange
         GetterSetter getterSetter3 = new GetterSetter(TEST_NOTE_1);
        // Act
         viewmodel.setNote(getterSetter3);
        // Assert
        verify(Noterepositoyr, Mockito.never()).insertdata((GetterSetter)org.mockito.Matchers.any(GetterSetter.class));

    }

    /*
        set note, null title, throw exception
     */

    @Test
    void setNote_nullTitle_throwException() throws Exception {

        // Arrange
        final GetterSetter getterSetter4 = new GetterSetter(TEST_NOTE_1);
        getterSetter4.setTitle(null);

        // Assert
       assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                // Act
                viewmodel.setNote(getterSetter4);
            }
        });

       System.out.println("Exception Generate sucessfully"+Exception.class);
    }

    /*
        update a note and observe row returned
     */

    @Test
    void UpdateNote_returnRow() throws Exception {

        // Arrange//
        GetterSetter getterSetter2 = new GetterSetter(TEST_NOTE_1);
        LivedataTestingClass<Resource<Integer>> liveDataTestingclass = new LivedataTestingClass<>();
        final int UpdateRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(UpdateRow,Update_Data));
        when(Noterepositoyr.UpdateData((GetterSetter)ArgumentMatchers.any(GetterSetter.class))).thenReturn(returnedData);
        // Act//
        viewmodel.setNote(getterSetter2);
        viewmodel.setIsNewNote(false);
        // viewmodel.setIsNewNote(true);//
        Resource<Integer> returnedValue = liveDataTestingclass.getValue(viewmodel.saveNote());
        System.out.println("Returned value: "+returnedValue);
        // Assert//
        assertEquals(Resource.success(UpdateRow,Update_Data), returnedValue);

    }


    @Test
    void dontReturnUpdateRowWithoutObserver() throws Exception {

        // Arrange
        GetterSetter getterSetter3 = new GetterSetter(TEST_NOTE_1);

        // Act
        viewmodel.setNote(getterSetter3);

        // Assert
        verify(Noterepositoyr, Mockito.never()).UpdateData((GetterSetter)org.mockito.Matchers.any(GetterSetter.class));

    }


    @Test
    void saveNote_shouldAllowSave_returnFalse() throws Exception {
        // Arrange
        GetterSetter note = new GetterSetter(TEST_NOTE_1);
        note.setTimestamp(null);

        // Act
        viewmodel.setNote(note);
        viewmodel.setIsNewNote(true);

        // Assert
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                viewmodel.saveNote();
            }
        });

        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
        System.out.println("ExceptionError:::::::::---"+exception.getMessage());
    }


     /*
        Retrieve list of notes
        observe list
        return list
     */

     /////////////////// plz try to understand this logic///////////////////////////////////////////
    //1)first of all i will set data into the getter setter type arraylist.
    // or keh le ki assign kardiya data(returndata) type ki arraylist ko...
    // 2)ab data ko observe kra database se or return kra data into returndata mai...
    // 3) Livedatautilclass ne observe kra getter setter class or data ko observe kra or set kar diya data
    // ko observedata type ke refrence variable mai.......

    @Test
    void retrieveNotes_returnNotesList() throws Exception {
        // Arrange
        List<GetterSetter> returnedData = TestiingValues.TEST_NOTES_LIST;
        LivedataTestingClass<List<GetterSetter>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<List<GetterSetter>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(Noterepositoyr.getNotes()).thenReturn(returnedValue);

        // Act
        notelistViewModel.getNotes();
        List<GetterSetter> observedData = liveDataTestUtil.getValue(notelistViewModel.ObserveAllNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }
    /*
        retrieve list of notes
        observe the list
        return empty list
     */

    @Test
    void retrieveNotes_returnEmptyNotesList() throws Exception {
        // Arrange
        List<GetterSetter> returnedData = new ArrayList<>();
        LivedataTestingClass<List<GetterSetter>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<List<GetterSetter>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(Noterepositoyr.getNotes()).thenReturn(returnedValue);

        // Act
        notelistViewModel.getNotes();
        List<GetterSetter> observedData = liveDataTestUtil.getValue(notelistViewModel.ObserveAllNotes());

        // Assert
        System.out.println("null data:::::::::::-------"+observedData);
        assertEquals(returnedData, observedData);
    }


    /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {
        // Arrange
        GetterSetter deletedNote = new GetterSetter(TestiingValues.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LivedataTestingClass<Resource<Integer>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(Noterepositoyr.deleteNote((GetterSetter)ArgumentMatchers.any(GetterSetter.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(notelistViewModel.deleteNote(deletedNote));

        // Assert
        assertEquals(returnedData, observedValue);
    }

    /*
        delete note
        observe Resource.error
        return Resource.error
     */
    @Test
    void deleteNote_observeResourceError() throws Exception {
        // Arrange
        GetterSetter deletedNote = new GetterSetter(TestiingValues.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(null, DELETE_FAILURE);
        LivedataTestingClass<Resource<Integer>> liveDataTestUtil = new LivedataTestingClass<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(Noterepositoyr.deleteNote((GetterSetter)ArgumentMatchers.any(GetterSetter.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(notelistViewModel.deleteNote(deletedNote));

        // Assert---@196a42c3,
        System.out.println("error response:::::::::::::::::---"+observedValue);
        assertEquals(returnedData, observedValue);
    }

}


