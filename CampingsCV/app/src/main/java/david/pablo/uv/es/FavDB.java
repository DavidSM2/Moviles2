package david.pablo.uv.es;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FavDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 7;
    private static String DATABASE_NAME = "CampingsDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String CAMPING_NAME = "campingName";
    public static String CATEGORIA = "categoria";
    public static String PROVINCIA = "provincia";
    public static String MUNICIPIO = "municipio";
    public static String CORREO = "correo";
    public static String DIRECCION = "direccion";
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + CAMPING_NAME + " TEXT, " + CATEGORIA + " TEXT, "
            + PROVINCIA + " TEXT, "+ MUNICIPIO + " TEXT, " + CORREO + " TEXT," + DIRECCION + " TEXT)";
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

    public void insertCamping (Camping camping) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPING_NAME, camping.getNombre());
        values.put(KEY_ID, camping.getId());
        values.put(CATEGORIA, camping.getCategoria());
        values.put(PROVINCIA, camping.getProvincia());
        values.put(MUNICIPIO, camping.getMunicipio());
        values.put(CORREO, camping.getCorreo());
        values.put(DIRECCION, camping.getDirecion());
        db.insert(TABLE_NAME,null,values);

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

    public ArrayList<Camping>  read_all_data () {
        ArrayList<Camping> campingList = new ArrayList<Camping>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Camping camping = new Camping(
                        Integer.parseInt(cursor.getString(0)),
                        0, //null
                        null, //null
                        0, //null
                        null, //null
                        null, //null
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );

                // Adding contact to list
                campingList.add(camping);
            } while (cursor.moveToNext());
        }

        // return contact list
        return campingList;
    }

    public Boolean isFav (int id) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        return cursor.moveToFirst();
    }


}
