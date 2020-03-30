package com.example.unittesting2.Models;

import android.util.Log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetterSetterUnittest {

   // public static final String TAG="equalCase";

   // unit testing does not support static methods so that's why don't used log unitTesting'
    //****************Only used system.out.println for unit testing for printing the messages******************

    //***************Compare to equal notes********************
    @Test
    void  isEqualTestCase_returnTrue() throws Exception {
        //Arrange:
        GetterSetter getterSetter = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter.setId(1);
        GetterSetter getterSetter1 = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter1.setId(1);

        //Act:

        //Assertion:
        Assertions.assertEquals(getterSetter,getterSetter1);
       //Log.d(TAG,"Both ids are equal");
        System.out.println("Both ids are same");

    }


    //***********************Compare notes with two different id**********************************

    @Test
    void Notes_with_different_Id_returnFlase() throws Exception {
        //Arrange:
        GetterSetter getterSetter = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter.setId(1);
        GetterSetter getterSetter1 = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter1.setId(2);

        //Act:

        //Assertion:
        Assertions.assertNotEquals(getterSetter,getterSetter1);
        //Log.d(TAG,"Both ids are equal");
        System.out.println("Both notes ids are different");

    }

    //***********************Compare notes with two different title**********************************

    @Test
    void  Notes_with_differentTitle_returnFlase() throws Exception {
        //Arrange:
        GetterSetter getterSetter = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter.setId(1);
        GetterSetter getterSetter1 = new GetterSetter("Jai mata di2","26/04/1997");
        getterSetter1.setId(1);

        //Act:

        //Assertion:
        Assertions.assertNotEquals(getterSetter,getterSetter1);
        //Log.d(TAG,"Both ids are equal");
        System.out.println("Both notes are different title ");

    }




    //***********************Compare notes with two different timestamp**********************************

    @Test
    void  Notes_with_differentTimeStamp_returnFlase() throws Exception {
        //Arrange:
        GetterSetter getterSetter = new GetterSetter("Jai mata di","26/04/1997");
        getterSetter.setId(1);
        GetterSetter getterSetter1 = new GetterSetter("Jai mata di2","26/04/1999");
        getterSetter1.setId(1);

        //Act:

        //Assertion:
        Assertions.assertNotEquals(getterSetter,getterSetter1);
        //Log.d(TAG,"Both ids are equal");
        System.out.println("Both notes are different timestamp ");

    }



}
