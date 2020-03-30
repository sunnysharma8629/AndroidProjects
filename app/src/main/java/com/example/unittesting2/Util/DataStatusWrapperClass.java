package com.example.unittesting2.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataStatusWrapperClass <T> {


    ////// This Class provide the more functionality of our application (UI)............./////////////////////
    ///////DublicateClasss////////////////////////

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public DataStatusWrapperClass(@NonNull Status status, @Nullable T data, @NonNull String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> DataStatusWrapperClass<T> success(@NonNull T data, @NonNull String message) {
        return new DataStatusWrapperClass<>(Status.SUCCESS, data, message);
    }

    public static <T> DataStatusWrapperClass<T> error( @Nullable T data, @NonNull String msg) {
        return new DataStatusWrapperClass<>(Status.ERROR, data, msg);
    }

    public static <T> DataStatusWrapperClass<T> loading(@Nullable T data) {
        return new DataStatusWrapperClass<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING}

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass() || obj.getClass() != DataStatusWrapperClass.class){
            return false;
        }

        DataStatusWrapperClass<T> resource = (DataStatusWrapperClass) obj;

        if(resource.status != this.status){
            return false;
        }

        if(this.data != null){
            if(resource.data != this.data){
                return false;
            }
        }

        if(resource.message != null){
            if(this.message == null){
                return false;
            }
            if(!resource.message.equals(this.message)){
                return false;
            }
        }

        return true;
    }

}
