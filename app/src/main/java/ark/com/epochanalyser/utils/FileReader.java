package ark.com.epochanalyser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileReader {

    public FileReader(){

    }

    public void startReadingEpoch(){

    }

    public static Date getDateFromString(String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);//2014-07-31 19:42:26
    }

}
