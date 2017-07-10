package cf.sadhu.citypicker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cf.sadhu.citypicker.domain.SearchHistory;

import static cf.sadhu.citypicker.db.SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_CITY_NAME;
import static cf.sadhu.citypicker.db.SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_OP_TIME;
import static cf.sadhu.citypicker.db.SearchHistoryContract.SearchHistoryEntry.COLUMN_NAME_PINYIN;
import static cf.sadhu.citypicker.db.SearchHistoryContract.SearchHistoryEntry.TABLE_NAME;

/**
 * Created by sadhu on 2017/7/9.
 * 描述: 搜索历史dao类
 */
public class SearchHistoryDao {

    private final SQLiteOpenHelper mHelper;

    public SearchHistoryDao(Context context) {
        mHelper = AppSQLiteOpenHelper.getInstance(context);
    }

    public List<SearchHistory> queryWithLimit(int limit) {
        List<SearchHistory> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor query = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_OP_TIME + " DESC",
                String.valueOf(limit));
        while (query.moveToNext()) {
            list.add(new SearchHistory(query.getString(query.getColumnIndex(COLUMN_NAME_CITY_NAME)),
                    query.getString(query.getColumnIndex(COLUMN_NAME_PINYIN)),
                    query.getLong(query.getColumnIndex(COLUMN_NAME_OP_TIME))));
        }
        query.close();
        db.close();
        return list;
    }

    public boolean createOrUpdate(SearchHistory searchHistory) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CITY_NAME, searchHistory.getCityName());
        contentValues.put(COLUMN_NAME_PINYIN, searchHistory.getCityPinYin());
        contentValues.put(COLUMN_NAME_OP_TIME, searchHistory.getOperateTime());
        long id = db.replace(TABLE_NAME, null, contentValues);
        db.close();
        return id != -1;
    }

    public void deleteAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
