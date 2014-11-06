package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ca.bcit.cst.team30.diary.access.DatabaseHandler;
import ca.bcit.cst.team30.diary.model.Entry;

public class CreateEntry extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
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
        DatabaseHandler db = new DatabaseHandler(this);
        String result;
        String title;
        EditText contentTitle = (EditText) findViewById(R.id.composeTitle);
        EditText content = (EditText) findViewById(R.id.composeContent);
        result = content.getText().toString();
        title = contentTitle.getText().toString();
        Entry entry = new Entry(title, result);

        db.addEntry(entry);

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}
