package ark.com.epochanalyser.analyser;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Analyser extends AsyncTask{
    private static final int EPOCH_COUNT_FOR_START = 150;
    private static final int EPOCH_COUNT_FOR_FINISH = 375;
    private static final int EPOCH_COUNT_FOR_ROLLING_PERCENTAGE = EPOCH_COUNT_FOR_START;
    private static final int ROLLING_PERCENT_THRESHOLD = 144;//150 * 0.96 = 144
    private static final int ACTIVITY_ID_TO_ANALYSE = 8;
    private static String mFileName = "testfiles/" + "ExampleAnalysedData.csv";
    private static final String DATE_FORMAT_EPOCH = "yyyy-MM-dd HH:mm:ss";
    private static final String SEPERATOR = ",";

    private Activity mActivity;
    private Date mStartTime;
    private Date mFinishTime;
    private String mDuration;
    private CircularFifoQueue<Epoch> mRollingQueue;
    private BufferedReader mBufferedReader;
    private int mFinishTriggerCount;
    private int mRollingPercent;

    private TextView mStartTextView;
    private TextView mFinishTextView;
    private TextView mDurationTextView;
    private ProgressBar mProgressBar;
    private int mStartTriggerCount = 0;
    private int mEpochCountForDuration;

    public Analyser(Activity activity, TextView startTextView, TextView finishTextView, TextView durationTextView,
                    ProgressBar progressBar) {
        mActivity = activity;
        mStartTextView = startTextView;
        mFinishTextView = finishTextView;
        mDurationTextView = durationTextView;
        mProgressBar = progressBar;
        mRollingQueue = new CircularFifoQueue<Epoch>(EPOCH_COUNT_FOR_ROLLING_PERCENTAGE);
        try {
            mBufferedReader = new BufferedReader(new InputStreamReader(mActivity.getAssets().open(mFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException, ParseException {
        String nextLine;
        Epoch epoch = null;
        Date possibleStartTime = null;
        Date possibleFinishTime = null;
        int epochsToRemove = 0;
        mBufferedReader.readLine();//skip first line
        while((nextLine = mBufferedReader.readLine()) != null){
            epoch = getNextEpoch(nextLine);
            if(epoch != null) {
                mRollingPercent = updateRollingPercent(epoch);
                //mark startTime
                if (mStartTime == null) {
                    if (mRollingPercent >= ROLLING_PERCENT_THRESHOLD
                            && mRollingQueue.size() == EPOCH_COUNT_FOR_ROLLING_PERCENTAGE) {
                        if (mStartTriggerCount == 0) {
                            possibleStartTime = mRollingQueue.get(0).getTimeStamp();//head
                            mEpochCountForDuration = getCurrentEpochCountForDuration();
                        }
                        mStartTriggerCount++;
                    } else {
                        mStartTriggerCount = 0;
                    }
                    if (mStartTriggerCount >= EPOCH_COUNT_FOR_START) {
                        mStartTime = possibleStartTime;
                        mEpochCountForDuration = mEpochCountForDuration + getCurrentEpochCountForDuration() - 1;
                    }
                }
                //mark finishTime, once startTime is marked
                else if (mFinishTime == null) {
                    if(epoch.getActivityId() == ACTIVITY_ID_TO_ANALYSE){
                        mEpochCountForDuration++;
                    }
                    if (mRollingPercent < ROLLING_PERCENT_THRESHOLD) {
                        mFinishTriggerCount++;
                        if(epoch.getActivityId() == ACTIVITY_ID_TO_ANALYSE){
                            epochsToRemove++;
                        }
                    } else {
                        //keep resetting...
                        possibleFinishTime = epoch.getTimeStamp();
                        mFinishTriggerCount = 0;
                        epochsToRemove = 0;
                    }
                    if (mFinishTriggerCount >= EPOCH_COUNT_FOR_FINISH) {
                        mFinishTime = possibleFinishTime;
                        mEpochCountForDuration = mEpochCountForDuration - epochsToRemove;
                        break;
                    }
                }
            }
        }
        //if startTime is found and finishTime is not found by end of file
        if(mStartTime != null && mFinishTime == null && epoch != null){
            mFinishTime = epoch.getTimeStamp();
        }
        mDuration = secondsToHHMM(mEpochCountForDuration * 10);
    }

    private int getCurrentEpochCountForDuration() {
        int epochCount = 0;
        Iterator<Epoch> iterator = mRollingQueue.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getActivityId() == ACTIVITY_ID_TO_ANALYSE) {
                epochCount++;
            }
        }
        return epochCount;
    }

    private int updateRollingPercent(Epoch epoch) {
        mRollingQueue.add(epoch);
        int epochCount = 0;
        if(mRollingQueue.size() == EPOCH_COUNT_FOR_ROLLING_PERCENTAGE) {
            Iterator<Epoch> iterator = mRollingQueue.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getActivityId() == ACTIVITY_ID_TO_ANALYSE) {
                    epochCount++;
                }
            }
        }
        return epochCount;
    }

    public Epoch getNextEpoch(String nextLine) throws IOException, ParseException {
        String[] lineContents = nextLine.split(SEPERATOR);
        int activityId = Integer.parseInt(lineContents[0]);//first column contains activityId
        Date timestamp = getDateFromString(lineContents[1]);//second column contains timestamp
        Epoch epoch = new Epoch(activityId, timestamp);
        return epoch;
    }

    public static Date getDateFromString(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT_EPOCH).parse(dateString);//2014-07-31 19:42:26
    }

    private String secondsToHHMM(int pTime) {
        String string = String.format("%02d:%02d", pTime / 3600, (pTime % 3600) / 60);
        return string;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        mStartTextView.setText((String)values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mStartTextView.setText(mStartTime.toString());
                mFinishTextView.setText(mFinishTime.toString());
                mDurationTextView.setText(mDuration);
            }
        });
        super.onPostExecute(o);
    }


    public static String getFileName() {
        return mFileName;
    }

    public static void setFileName(String fileName) {
        mFileName = "testfiles/" + fileName;
    }
}
