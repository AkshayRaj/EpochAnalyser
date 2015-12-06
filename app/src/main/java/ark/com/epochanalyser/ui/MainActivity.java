package ark.com.epochanalyser.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ark.com.epochanalyser.R;
import ark.com.epochanalyser.utils.Analyser;

public class MainActivity extends Activity{
    private Context mContext;
    private TextView mStartTextView;
    private TextView mFinishTextView;
    private TextView mDurationTextView;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mStartTextView = (TextView) findViewById(R.id.textview_start);
        mFinishTextView = (TextView) findViewById(R.id.textview_finish);
        mDurationTextView = (TextView) findViewById(R.id.textview_duration);
        mStartButton = (Button) findViewById(R.id.button_start);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analyser analyser = new Analyser((Activity) mContext, mStartTextView, mFinishTextView, mDurationTextView);
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
