package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
	private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

		dataSource = new EntryDataSource(this);
		dataSource.open();

		final long entryId = getIntent().getExtras().getLong(TimelineFragment.EXTRA_ID);
		entry = dataSource.getEntry(entryId);



        // Title
        final TextView title = (TextView) findViewById(R.id.entry_title);
        title.setText(entry.getTitle());

        // Scrollable text content
        final TextView content = (TextView) findViewById(R.id.entry_content);
        content.setText(entry.getContent());

        // Image
        /*try {
            //ContextWrapper cw = new ContextWrapper(getApplicationContext());
            /*File file = new File(entry.getFilePath());
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ImageView image = (ImageView) findViewById(R.id.entry_photo);
            image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

		final String imagePath = entry.getFilePath();
		if (imagePath != null) {
			Uri selectedImage = Uri.parse(imagePath);
			ImageView image = (ImageView) findViewById(R.id.entry_photo);
			image.setImageURI(selectedImage);
		}
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
        } else if( id == R.id.action_edit ) {
            editEntry();
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

    public void editEntry() {
        Intent intent = new Intent(this, EditEntry.class);
        intent.putExtra("id", entry.getId());
        intent.putExtra("title", entry.getTitle());
        intent.putExtra("content", entry.getContent());
        intent.putExtra("image", entry.getFilePath());
        Log.d("LOG", "Passing intent with title: " + entry.getTitle());
        startActivity(intent);

        finish();
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
