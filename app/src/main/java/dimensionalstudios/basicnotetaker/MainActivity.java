package dimensionalstudios.basicnotetaker;

import android.content.ContentValues;
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
import android.widget.ListView;

//Use loader interface to create asynchronous operations!

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("New note");
        //query notes table
        //Cursor cursor = getContentResolver().query(noteProvider.CONTENT_URI,
        //        dbOpenHelper.ALL_COLUMNS, null, null, null, null);
        //Where data is coming from
        String[] from = {dbOpenHelper.NOTE_TEXT};
        //ID of text view that will be used in a view that is delivered by the SDK
        //What will display with
        int[] to = {android.R.id.text1};

        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to);

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
