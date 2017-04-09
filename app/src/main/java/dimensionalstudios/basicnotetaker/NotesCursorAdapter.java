package dimensionalstudios.basicnotetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by David on 4/8/2017.
 * A custom cursor adapter that supports multi-line lists.
 */

public class NotesCursorAdapter extends CursorAdapter{

    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    //Reads and inflates layout if needed
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item,
                parent, false);
    }

    @Override
    //Handles the multi-line note.
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString(cursor.getColumnIndex(dbOpenHelper.NOTE_TEXT));
        //\n
        int pos = noteText.indexOf(10);
        if(pos != -1){
            //Add ... for stuff that wraps with a new line
            noteText = noteText.substring(0, pos) + "...";
        }

        TextView textView = (TextView) view.findViewById(R.id.tvNote);
        textView.setText(noteText);
    }
}
