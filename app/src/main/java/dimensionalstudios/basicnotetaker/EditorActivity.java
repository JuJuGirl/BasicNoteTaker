package dimensionalstudios.basicnotetaker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;

    private String noteFilter;  //used in SQL
    private String oldText;     //existing text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        editor = (EditText) findViewById(R.id.editText);
        Uri uri = intent.getParcelableExtra(noteProvider.CONTENT_ITEM_TYPE);
        if(uri == null){
            //Insert
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note);
        }
        else{
            action = Intent.ACTION_EDIT;
            noteFilter = dbOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            //Location of whatever matched
            Cursor cursor = getContentResolver().query(uri, dbOpenHelper.ALL_COLUMNS,
                    noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(dbOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case android.R.id.home:
                finishEdit();
                break;
        }
        return true;
    }

    private void finishEdit(){
        String newText = editor.getText().toString();
        newText = newText.trim();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        switch(action){
            case Intent.ACTION_INSERT:
                if(newText.isEmpty()){
                    Toast toast = Toast.makeText(context, "Empty note not saved.", duration);
                    toast.show();
                    setResult(RESULT_CANCELED);
                }
                else{
                    Toast toast = Toast.makeText(context, "Note inserted!", duration);
                    toast.show();
                    insertNote(newText);
                }
        }
        //Go back to the parent activity
        finish();
    }

    private void insertNote(String newText) {
        ContentValues values = new ContentValues();
        //put/get - like a map!
        //Key = name of column you're assigning a value
        //Value = value
        values.put(dbOpenHelper.NOTE_TEXT, newText);
        Uri noteUri = getContentResolver().insert(noteProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEdit();
    }

}
