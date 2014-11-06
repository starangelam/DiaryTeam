package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewEntry extends Activity {
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        // Creating parent viewgroup for other views to be added on
        ViewGroup entryParent = (ViewGroup) findViewById(R.id.entry_container);

        // Title
        view = LayoutInflater.from(this).inflate(R.layout.title, entryParent, true);
        TextView title = (TextView) view.findViewById(R.id.entry_title);
        title.setText("Insert Title here");

        // Scrollable text content
        view = LayoutInflater.from(this).inflate(R.layout.content, entryParent, true);
        TextView content = (TextView) view.findViewById(R.id.entry_content);
        content.setText("Insert\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Content here");

        // Scrollable image content
        view = LayoutInflater.from(this).inflate(R.layout.images, entryParent, true);
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
}
