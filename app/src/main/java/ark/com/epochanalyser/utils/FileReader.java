package ark.com.epochanalyser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class FileReader {
    private static final String DATE_FORMAT_EPOCH = "yyyy-MM-dd HH:mm:ss";

    Date mStartTime;
    Date mFinishTime;
    Date mDuration;
    Queue<Epoch> mStartQueue;
    Queue<Epoch> mFinishQueue;

    public FileReader(){
        mStartQueue = new ArrayBlockingQueue<Epoch>(150);
        mFinishQueue = new ArrayBlockingQueue<Epoch>(375);
    }

    public void readNextColumn(){

    }

    public static Date getDateFromString(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT_EPOCH).parse(dateString);//2014-07-31 19:42:26
    }

}
