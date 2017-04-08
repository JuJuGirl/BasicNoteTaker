package dimensionalstudios.basicnotetaker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

//Use loader interface to create asynchronous operations!

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private NotesCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //insertNote("New note");

        /*query notes table
          Cursor cursor = getContentResolver().query(noteProvider.CONTENT_URI,
                  dbOpenHelper.ALL_COLUMNS, null, null, null, null);*/

        //Where data is coming from
        String[] from = {dbOpenHelper.NOTE_TEXT};
        //ID of text view that will be used in a view that is delivered by the SDK
        //What will display with
        //int[] to = {android.R.id.text1};
        int[] to = {R.id.tvNote};
        cursorAdapter = new NotesCursorAdapter(this, null, 0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        //id, arguments, implementation of LoaderCallbacks interface
        getLoaderManager().initLoader(0, null, this);
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        //put/get - like a map!
        //Key = name of column you're assigning a value
        //Value = value
        values.put(dbOpenHelper.NOTE_TEXT, noteText);
        Uri noteUri = getContentResolver().insert(noteProvider.CONTENT_URI, values);

        //Debug
        Log.d("MainActivity", "Inserted note"+noteUri.getLastPathSegment());
    }

    //Menu and menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        switch(id){
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllNodes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Test methods
    private void insertSampleData() {
        insertNote("First");
        insertNote("Multi-line\nnote");
        insertNote("A super long node that may or may not wrap the bounds of the application window but with tablets this may not happen");
        restartLoader();

    }

    private void deleteAllNodes() {
        //Confirm? Dialogue box
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //delete all
                            getContentResolver().delete(noteProvider.CONTENT_URI, null, null);
                            restartLoader();
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void restartLoader() {
        //Called every time we insert data
        getLoaderManager().restartLoader(0,null,this);
    }

    //Loader methods

    @Override
    //called whenever data is needed from the content provider
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.content.CursorLoader(this, noteProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    //event method - called when data comes back
    //Takes data represented by "Cursor data" and passes it to the adapter
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    //When data needs to be wiped
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
