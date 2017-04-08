package dimensionalstudios.basicnotetaker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("New note");
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
