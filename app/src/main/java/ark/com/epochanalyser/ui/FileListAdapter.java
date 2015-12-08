package ark.com.epochanalyser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ark.com.epochanalyser.R;
import ark.com.epochanalyser.analyser.Analyser;

public class FileListAdapter extends BaseAdapter {
        private List<String> mFileList;
        private String[] mFileNames;
        private LayoutInflater mInflater;
        private Context mContext;

        public FileListAdapter(Context context) {
            mContext = context;
            try {
                mFileList = new ArrayList<String>(Arrays.asList(mContext.getAssets().list("testfiles")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInflater = LayoutInflater.from(mContext);
        }

        public void setFileList(List<String> file){
            mFileList = file;
        }
        @Override
        public int getCount() {
            return mFileList.size();
        }

        @Override
        public String getItem(int position) {
            return mFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CategoryViewHolder categoryViewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_file_list, parent, false);
                categoryViewHolder = new CategoryViewHolder(convertView);
                convertView.setTag(categoryViewHolder);
            } else {
                categoryViewHolder = (CategoryViewHolder) convertView.getTag();
            }

            String fileName = getItem(position);
            categoryViewHolder.fileName.setText(fileName);
            return convertView;

        }

        private class CategoryViewHolder {
            TextView fileName;

            public CategoryViewHolder(View item) {
                fileName = (TextView) item.findViewById(R.id.list_item);
            }
        }
}
