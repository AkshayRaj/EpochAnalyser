package ark.com.epochanalyser.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Epoch {
    int mActivityId;
    Date mTimeStamp;

    public Epoch(int activityId, Date timestamp){
        mActivityId = activityId;
        mTimeStamp = timestamp;
    }
}
