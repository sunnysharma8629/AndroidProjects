package unittesting2;

import com.example.unittesting2.Models.GetterSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestiingValues {

    public static final String TIMESTAMP_1 = "05-2019";
    public static final GetterSetter TEST_NOTE_1 = new GetterSetter( "It's garbage day tomorrow.", TIMESTAMP_1);

    public static final String TIMESTAMP_2 = "06-2019";
    public static final GetterSetter TEST_NOTE_2 = new GetterSetter( "Buy an anniversary gift.", TIMESTAMP_2);

    public static final List<GetterSetter> TEST_NOTES_LIST = Collections.unmodifiableList(
            new ArrayList<GetterSetter>(){{
                add(new GetterSetter (1,"It's garbage day tomorrow.", TIMESTAMP_1));
                add(new GetterSetter(2,"Buy an anniversary gift.", TIMESTAMP_2));
            }}
    );

}
