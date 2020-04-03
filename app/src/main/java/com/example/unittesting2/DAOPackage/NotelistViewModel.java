package com.example.unittesting2.DAOPackage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;

import java.util.List;

public class NotelistViewModel extends AndroidViewModel {

    private Repository notelistrepository;

    private MediatorLiveData<List<GetterSetter>> notes = new MediatorLiveData<>();


    public NotelistViewModel(@NonNull Application application) {
        super(application);
        notelistrepository = Repository.getInstance(application);

    }

    public LiveData<Resource<Integer>> deleteNote(final GetterSetter note) throws Exception{
        return notelistrepository.deleteNote(note);
    }

    public LiveData<List<GetterSetter>> ObserveAllNotes(){
        return notes;
    }


    public void getNotes(){
        final LiveData<List<GetterSetter>> source = notelistrepository.getNotes();
        notes.addSource(source, new Observer<List<GetterSetter>>() {
            @Override
            public void onChanged(List<GetterSetter> getterSetters) {

                if(getterSetters != null){
                    notes.setValue(getterSetters);
                }
                notes.removeSource(source);
            }

        });
    }
}
