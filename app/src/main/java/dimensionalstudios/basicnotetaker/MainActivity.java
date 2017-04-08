package dimensionalstudios.basicnotetaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("New note");
        //query notes table
        Cursor cursor = getContentResolver().query(noteProvider.CONTENT_URI,
                dbOpenHelper.ALL_COLUMNS, null, null, null, null);
        //Where data is coming from
        String[] from = {dbOpenHelper.NOTE_TEXT};
        //ID of text view that will be used in a view that is delivered by the SDK
        //What will display with
        int[] to = {android.R.id.text1};
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, from, to);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

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
}
