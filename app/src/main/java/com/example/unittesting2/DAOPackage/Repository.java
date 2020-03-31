package com.example.unittesting2.DAOPackage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.unittesting2.Models.GetterSetter;
import com.example.unittesting2.UI.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Repository {

    public static final String Insert_Sucess="data-inserted";
    public static final String Insert_Failure="data-notinserted";
    public static final String Insert_Update="Update_Data_Inserted";
    public static final String Update_Failure="Not_updated";
    public static final String DELETE_SUCCESS = "Delete success";
    public static final String DELETE_FAILURE = "Delete failure";
    public static final String INVALID_NOTE_ID = "Invalid id. Can't delete note";
    public static final String NOTE_TITLE_NULL = "Note title cannot be null";

    private int TimeDelay = 0;
    private TimeUnit  t1= TimeUnit.SECONDS;


    private NoteDatabaseCreateClass noteDatabaseCreateClass;

    public Repository(Context context) {
        noteDatabaseCreateClass = NoteDatabaseCreateClass.getInstance(context);
    }


    public Flowable<Resource<Integer>> insertdata(final GetterSetter getterSetter)throws Exception {

        CheckTitle(getterSetter);
        return noteDatabaseCreateClass.getDAOObjects().insertnote(getterSetter)
                .delaySubscription(TimeDelay,t1)
                .map(new Function<Long,Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        long l1 = aLong;
                        return (int)l1;
                    }
                }).onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        return -1;
                    }
                }).map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(Integer integer) throws Exception {
                        if (integer>0)
                        {
                            return Resource.success(integer,Insert_Sucess);
                        }
                        return Resource.error(null,Insert_Failure);
                    }
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();

    }


    public Flowable<Resource<Integer>> UpdateData(GetterSetter getterSetter1)throws Exception
    {
        CheckTitle(getterSetter1);
        return noteDatabaseCreateClass.getDAOObjects().updatenote(getterSetter1)
                .delaySubscription(TimeDelay,t1)
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        return -1;
                    }
                })
                .map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(Integer integer) throws Exception {
                        if (integer>0)
                        {
                            return Resource.success(integer,Insert_Update);
                        }
                        return Resource.error(integer,Update_Failure);
                    }
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();

    }


    private void CheckTitle(GetterSetter getterSetter)throws Exception{
        if (getterSetter.getTitle()== null) {
            throw new Exception(NOTE_TITLE_NULL);
        }
    }


    public LiveData<Resource<Integer>> deleteNote(final GetterSetter note) throws Exception{

        checkId(note);

        return LiveDataReactiveStreams.fromPublisher(
                noteDatabaseCreateClass.getDAOObjects().deletenote(note)
                        .onErrorReturn(new Function<Throwable, Integer>() {
                            @Override
                            public Integer apply(Throwable throwable) throws Exception {
                                return -1;
                            }
                        })
                        .map(new Function<Integer, Resource<Integer>>() {
                            @Override
                            public Resource<Integer> apply(Integer integer) throws Exception {
                                if(integer > 0){
                                    return Resource.success(integer, DELETE_SUCCESS);
                                }
                                return Resource.error(null, DELETE_FAILURE);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<List<GetterSetter>> getNotes(){
        return noteDatabaseCreateClass.getDAOObjects().GetNotes();
    }

    private void checkId(GetterSetter note) throws Exception{
        if(note.getId() < 0){
            throw new Exception(INVALID_NOTE_ID);
        }
    }


}
