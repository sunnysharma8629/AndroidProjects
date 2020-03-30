package com.example.unittesting2.DateUtil;

import com.example.unittesting2.Util.DateConverter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static com.example.unittesting2.Util.DateConverter.GET_MONTH_ERROR;
import static com.example.unittesting2.Util.DateConverter.getMonthFromNumber;
import static com.example.unittesting2.Util.DateConverter.monthNumbers;
import static com.example.unittesting2.Util.DateConverter.months;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTest {

    private static final String today = "02-2020";

    @Test
    public void testGetCurrentTimestamp_returnedTimestamp(){
       // ***************** this methods for used only executable codes**********************************
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                assertEquals(today, DateConverter.getCurrentTimeStamp());
                System.out.println("Timestamp is generated correctly");

            }
        });

    }


    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11})
    public void getMonthFromNumber_returnSuccess(int monthNumber, TestInfo testInfo, TestReporter testReporter){
        assertEquals(months[monthNumber], DateConverter.getMonthFromNumber(monthNumbers[monthNumber]));
        System.out.println(monthNumbers[monthNumber] + " : " + months[monthNumber]);
    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10,11})
    public void testGetMonthFromNumber_returnError(int monthNumber, TestInfo testInfo, TestReporter testReporter){
        int randomInt = new Random().nextInt(90) + 13;
        assertEquals(getMonthFromNumber(String.valueOf(monthNumber * randomInt)), GET_MONTH_ERROR);
        System.out.println(monthNumbers[monthNumber] + " : " + GET_MONTH_ERROR);
    }

}

