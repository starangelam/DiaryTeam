package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.model.Entry;

public class CreateEntry extends Activity {
	private EntryDataSource datasource;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

		datasource = new EntryDataSource(this);
		datasource.open();
    }

	@Override
	public void onResume()
	{
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		datasource.close();
		super.onPause();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.composeEntry:
                createEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createEntry() {
		final EditText contentTitle;
        final EditText content;
		final String result;
		final String title;
		final Entry entry;

		contentTitle = (EditText) findViewById(R.id.composeTitle);
		content = (EditText) findViewById(R.id.composeContent);
        result = content.getText().toString();
        title = contentTitle.getText().toString();

        entry = new Entry(title, result);
        datasource.createEntry(entry);

        finish();
    }
}
