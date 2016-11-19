package cl.telematica.android.certamen3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by simon on 18-11-2016.
 */

public class DataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favoritosDB";

    private String sqlCreate = "CREATE TABLE 'favoritos' (" +
            "'title' TEXT, " +
            "'id' INTEGER, " +
            "'link' TEXT, " +
            "'author' TEXT, " +
            "'publishedDate' TEXT, " +
            "'content' TEXT, " +
            "'img' TEXT, " +
            "'isFavorite' TEXT)";
    public DataBase (Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST 'favoritos'");
        onCreate(db);
    }
}
