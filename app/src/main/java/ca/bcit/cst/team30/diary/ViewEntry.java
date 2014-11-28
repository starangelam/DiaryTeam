package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
	private static final int REQUEST_CODE_EDIT_ENTRY = 1;

	private EntryDataSource dataSource;
	private long entryId;
	private TextView title;
	private TextView content;
	private ImageView image;
	private boolean isEntryModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);


		dataSource = new EntryDataSource(this);
		dataSource.open();

		entryId = getIntent().getExtras().getLong(TimelineFragment.EXTRA_ID);
        title = (TextView) findViewById(R.id.entry_title);
        content = (TextView) findViewById(R.id.entry_content);
		image = (ImageView) findViewById(R.id.entry_photo);

		isEntryModified = false;

		displayEntry();
    }

	private void displayEntry() {
		final Entry entry;
		final String imagePath;

		entry = dataSource.getEntry(entryId);
		title.setText(entry.getTitle());
		content.setText(entry.getContent());
		imagePath = entry.getFilePath();
		if (imagePath != null) {
			final Uri selectedImage = Uri.parse(imagePath);
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
		switch (id) {
			case R.id.action_edit:
				editEntry();
				break;
			case R.id.action_delete:
				deleteEntry();
				break;
		}
        return super.onOptionsItemSelected(item);
    }

	private void deleteEntry() {
		dataSource.deleteEntry(entryId);
		this.setResult(RESULT_OK);
		finish();
	}

    public void editEntry() {
        Intent intent = new Intent(this, EditEntry.class);
        intent.putExtra("id", entryId);
		startActivityForResult(intent, REQUEST_CODE_EDIT_ENTRY);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("debug", "result returned.");
		if (requestCode == REQUEST_CODE_EDIT_ENTRY && resultCode == RESULT_OK) {
			Log.d("debug", "edit entry successful.");
			displayEntry();
			isEntryModified = true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK && isEntryModified)) {
			this.setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
