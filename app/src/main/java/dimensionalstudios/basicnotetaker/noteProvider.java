package dimensionalstudios.basicnotetaker;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by David on 4/8/2017.
 */

public class noteProvider extends ContentProvider {

    //IDs the content provider to the framework - only 1 app can use a particular authority -
    //install will fail if this is already present in a device
    private static final String AUTHORITY = "com.dimensionalstudios.basicnotetaker.noteprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);

    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    //Parse URI and tell you which operation was requested
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_ITEM_TYPE = "node";


    //executes the first time anything is called from this class
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        //Looking for a particular note
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        dbOpenHelper helper = new dbOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    //Gets all notes or specific note
    //"selection" is a particular value
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //If note selected, only return that row
        if(uriMatcher.match(uri) == NOTES_ID){
            selection = dbOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        //Table, columns, selection, x, x, x, sort (latest note listed first
        return database.query(dbOpenHelper.TABLE_NOTES, dbOpenHelper.ALL_COLUMNS, selection,
                null, null, null, dbOpenHelper.NOTE_CREATED+" DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    //URI, name-value pairs
    public Uri insert(Uri uri, ContentValues values) {
        //Get primary key
        long id = database.insert(dbOpenHelper.TABLE_NOTES, null, values);
        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Table, where, selection
        return database.delete(dbOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return database.update(dbOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
