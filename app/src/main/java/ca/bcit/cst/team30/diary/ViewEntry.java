package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.model.Entry;

public class ViewEntry extends Activity {
    Integer[] imageIDs = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };
    private View view;

	private EntryDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

		dataSource = new EntryDataSource(this);
		dataSource.open();



		final long entryId = getIntent().getExtras().getLong(TimelineFragment.EXTRA_ID);
		final Entry entry = dataSource.getEntry(entryId);

		// TODO bug gridView return null pointer exception
//        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridimages);
//        gridView.setAdapter(new ImageAdapter(this));
//        gridView.setExpanded(true);

        // Creating parent viewgroup for other views to be added on
        final ViewGroup entryParent = (ViewGroup) findViewById(R.id.entry_container);

        // Title
        view = LayoutInflater.from(this).inflate(R.layout.title, entryParent, true);
        final TextView title = (TextView) view.findViewById(R.id.entry_title);
        title.setText(entry.getTitle());

        view = LayoutInflater.from(this).inflate(R.layout.imagegrid, entryParent, true);

        // Scrollable text content
        view = LayoutInflater.from(this).inflate(R.layout.content, entryParent, true);
        final TextView content = (TextView) view.findViewById(R.id.entry_content);
        content.setText(entry.getContent());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(R.drawable.ic_launcher);
            return imageView;
        }
    }


	@Override
	public void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		dataSource.close();
		super.onPause();
	}
}
