package com.example.unittesting2.DAOPackage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;
import com.example.unittesting2.Util.NoteInsertUpdateHelper;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;

import static android.content.Intent.ACTION_INSERT;

public class NoteViewModel extends AndroidViewModel {

    private static final String TAG = "NoteViewModel";
    public static final String NO_CONTENT_ERROR = "Can't save note with no content";

    public enum ViewState {VIEW, EDIT}
   // private MutableLiveData<GetterSetter> note  = new MutableLiveData<>();
    private MutableLiveData<GetterSetter> gettersetter = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    //private MediatorLiveData<List<GetterSetter>> notes = new MediatorLiveData<>();
    private boolean isNewNote;
    private Subscription updateSubscription, insertSubscription;
    private Repository repository;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);

    }

    public LiveData<Resource<Integer>> insertdata()throws Exception{

        return LiveDataReactiveStreams.fromPublisher
                (repository.insertdata(gettersetter.getValue())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        insertSubscription = subscription;
                    }
                })
                );

    }

    public LiveData<Resource<Integer>> updatenote()throws Exception
    {
        return LiveDataReactiveStreams.fromPublisher
                (repository.UpdateData(gettersetter.getValue())
                 .doOnSubscribe(new Consumer<Subscription>() {
                 @Override
                   public void accept(Subscription subscription) throws Exception {
                     updateSubscription = subscription;

                 }
        }));
    }


    public LiveData<GetterSetter> observenote()
    {
        return gettersetter;
    }


    public LiveData<ViewState> observeViewState(){
        return viewState;
    }

    public void setViewState(ViewState viewState){
        this.viewState.setValue(viewState);
    }

    public void setIsNewNote(boolean isNewNote){
        this.isNewNote = isNewNote;
    }

    public void setNote(GetterSetter getterSetter1)throws Exception
    {
        if (getterSetter1.getTitle()== null ||getterSetter1.getTitle().equals("")){
            throw new Exception(  "Title has been null");
        }
        this.gettersetter.setValue(getterSetter1);

    }


    public void updateNote1(String title, String content) throws Exception{
        if(title == null || title.equals("")){
            throw new NullPointerException("Title can't be null");
        }
        String temp = removeWhiteSpace(content);
        if(temp.length() > 0){
            GetterSetter updatedNote = new GetterSetter(gettersetter.getValue());
            updatedNote.setTitle(title);
            //updatedNote.setTitle(content);
            updatedNote.setTimestamp(content);

            gettersetter.setValue(updatedNote);
        }
    }



    public LiveData<Resource<Integer>> saveNote() throws Exception{

        if(!shouldAllowSave()){
            throw new Exception(NO_CONTENT_ERROR);
        }
        cancelPendingTransactions();

        return new NoteInsertUpdateHelper<Integer>(){

            @Override
            public void setNoteId(int noteId) {
                isNewNote = false;
                GetterSetter currentNote = gettersetter.getValue();
                currentNote.setId(noteId);
                gettersetter.setValue(currentNote);
            }

            @Override
            public LiveData<Resource<Integer>> getAction() throws Exception {
                if(isNewNote){
                    return insertdata();
                }
                else{
                    return updatenote();
                }
            }

            @Override
            public String defineAction() {
                if(isNewNote){
                    return ACTION_INSERT;
                }
                else{
                    return ACTION_UPDATE;
                }
            }

            @Override
            public void onTransactionComplete() {
                updateSubscription = null;
                insertSubscription = null;
            }
        }.getAsLiveData();
    }

    private void cancelPendingTransactions(){
        if(insertSubscription != null){
            cancelInsertTransaction();
        }
        if(updateSubscription != null){
            cancelUpdateTransaction();
        }
    }

    private void cancelUpdateTransaction(){
        updateSubscription.cancel();
        updateSubscription = null;
    }

    private void cancelInsertTransaction(){
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private boolean shouldAllowSave() throws Exception{
        try{
            return removeWhiteSpace((gettersetter.getValue()).getTimestamp()).length() > 0;
        }catch (NullPointerException e){
            throw new Exception(NO_CONTENT_ERROR);
        }
    }


    private String removeWhiteSpace(String string){
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        return string;
    }


    public boolean shouldNavigateBack(){
        if(viewState.getValue() == ViewState.VIEW){
            return true;
        }
        else{
            return false;
        }
    }



}

