package ark.com.epochanalyser.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ark.com.epochanalyser.R;
import ark.com.epochanalyser.analyser.Analyser;

public class MainActivity extends Activity{
    private Context mContext;
    private TextView mStartTextView;
    private TextView mFinishTextView;
    private TextView mDurationTextView;
    private Button mStartButton;
    private ListView mFileListView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mStartTextView = (TextView) findViewById(R.id.textview_start);
        mFinishTextView = (TextView) findViewById(R.id.textview_finish);
        mDurationTextView = (TextView) findViewById(R.id.textview_duration);
        mStartButton = (Button) findViewById(R.id.button_start);
        mFileListView = (ListView) findViewById(R.id.list_view_file);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mFileListView.setAdapter(new FileListAdapter(mContext));
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String fileName = mFileListView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),
                        fileName, Toast.LENGTH_SHORT).show();
                Analyser.setFileName(fileName);
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analyser analyser = new Analyser((Activity) mContext, mStartTextView, mFinishTextView, mDurationTextView, mProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                analyser.execute();
            }
        });
    }

/***************************** getters & setters *********************************/
    public TextView getStartTextView() {
        return mStartTextView;
    }

    public void setStartTextView(TextView startTextView) {
        mStartTextView = startTextView;
    }

    public TextView getFinishTextView() {
        return mFinishTextView;
    }

    public void setFinishTextView(TextView finishtextview) {
        mFinishTextView = finishtextview;
    }

    public TextView getDurationTextView() {
        return mDurationTextView;
    }

    public void setDurationTextView(TextView durationTextView) {
        mDurationTextView = durationTextView;
    }

    public Button getStartButton() {
        return mStartButton;
    }

    public void setStartButton(Button startButton) {
        mStartButton = startButton;
    }

}
