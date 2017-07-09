package cf.sadhu.citypicker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:
 */
public class AppSQLiteOpenHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CityPicker.db";
    private static AppSQLiteOpenHelper mInstance;

    private AppSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppSQLiteOpenHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchHistoryContract.SearchHistoryEntry.TABLE_NAME + " (" +
                    SearchHistoryContract.SearchHistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_CITY_NAME + TEXT_TYPE + COMMA_SEP +
                    SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_OP_TIME + TEXT_TYPE + COMMA_SEP +
                    SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_PINYIN + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchHistoryContract.SearchHistoryEntry.TABLE_NAME;
}
