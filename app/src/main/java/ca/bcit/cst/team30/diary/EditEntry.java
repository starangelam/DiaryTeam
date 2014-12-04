package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.Toast;
import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.model.Entry;


public class EditEntry extends Activity {

    private EntryDataSource datasource;
	private File photoFile;
    private Uri selectedImage;
    private Entry entry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        datasource = new EntryDataSource(this);
        datasource.open();

        Bundle bundle = getIntent().getExtras();

        entry = datasource.getEntry(bundle.getLong("id"));
        Log.d("LOG", "Receiving intent with title: " + entry.getTitle());

        final EditText title = (EditText) findViewById(R.id.editTitle);
        title.setText(entry.getTitle());

        final EditText content = (EditText) findViewById(R.id.editContent);
        content.setText(entry.getContent());

		final ImageView image = (ImageView) findViewById(R.id.editphoto);
		final String imagePath = entry.getFilePath();
		if (imagePath != null) {
			selectedImage = Uri.parse(imagePath);
			image.setImageURI(selectedImage);
		}

        String datetext = entry.getCreationDateString();
        TextView t =(TextView)findViewById(R.id.editdatebar);
        t.setText(datetext);

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
                editEntry();
                return true;
            case R.id.getphoto:
                getPhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getPhoto() {

        final CharSequence[] items = {"From Gallery", "From Camera"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo Options ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item == 0) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.d("LOG", "Could not create file");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(pickPhoto, 1);
                    }

                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.d("LOG", "Could not create file");
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, 0);
                        }
                    }
                }
            }
        }).show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		final String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);


        // Save a file: path for use with ACTION_VIEW intents
		final String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("LOG", mCurrentPhotoPath);

        return image;
    }

    public void editEntry() {
        final EditText contentTitle;
        final EditText content;
		final TextView dateText;

		final String rawDate;
		final Date date;
        final String result;
        final String title;
		final String imagePath;

		dateText = (TextView) findViewById(R.id.editdatebar);
		rawDate = dateText.getText().toString();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Entry.DATE_FORMAT_NOW);
			date = sdf.parse(rawDate);
			entry.setCreationDate(date);

		} catch (ParseException pe) {
			Toast toast  = Toast.makeText(this, "date should be in format of " + Entry.DATE_FORMAT_NOW, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			pe.printStackTrace();
			return;
		}

		contentTitle = (EditText) findViewById(R.id.editTitle);
        content = (EditText) findViewById(R.id.editContent);

        result = content.getText().toString();
        title = contentTitle.getText().toString();

        entry.setTitle(title);
        entry.setContent(result);

		imagePath = (selectedImage == null) ? null : selectedImage.toString();
        entry.setFilePath(imagePath);

        datasource.editEntry(entry);

		this.setResult(RESULT_OK);

		finish();
    }

    //Receiving data back from the photo intent
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageview = (ImageView) findViewById(R.id.editphoto);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    selectedImage = Uri.fromFile(photoFile);
                    imageview.setImageURI(selectedImage);
                    Log.d("LOG", "I'm at case 0!");
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                    Log.d("LOG", "I'm at case 1!");
                }
                break;
        }
    }

}
