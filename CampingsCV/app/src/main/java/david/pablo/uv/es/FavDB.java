package david.pablo.uv.es;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class FavDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "CampingsDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String CAMPING_NAME = "campingName";
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + CAMPING_NAME + " TEXT)";
    private static String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FavDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public void insertCamping (String name, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPING_NAME,name);

        long newRowId = db.insert(TABLE_NAME,null,values);

        db.close();
    }
    public void deleteCamping (int id) {
        // Define 'where' part of query.
        String selection = KEY_ID + " LIKE ?";
        String idcast = id +"";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {idcast};

        // Issue SQL statement.
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, selection,selectionArgs);

        db.close();
    }

    public Cursor read_all_data () {
        SQLiteDatabase db = this.getReadableDatabase();

        /*
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                KEY_ID,
                CAMPING_NAME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = CAMPING_NAME + " = ?";

        */

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CAMPING_NAME + " DESC";

        Cursor cursor = db.query(
                CAMPING_NAME,           // The table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        db.close();

        return cursor;
    }
    /*
    public Cursor isFav (int id) {

    }
    */

}
