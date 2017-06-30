package cf.sadhu.citypicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cf.sadhu.citypicker.domain.City;

/**
 * Created by sadhu on 2017/6/29.
 * 描述
 */
public class DBManager {
    private static final String DB_DIR_NAME = "databases";
    private static final String DB_NAME = "china_cities.db";
    private static final String TABLE_NAME = "city";
    private static final String COLUMN1 = "name";
    private static final String COLUMN2 = "pinyin";

    public boolean initCityDB(Context ctx) {
        File dbFile = new File(ctx.getFilesDir().getParentFile(), DB_DIR_NAME);
        if (!dbFile.exists() || !dbFile.isDirectory()) {
            return dbFile.mkdir() && copyDB(ctx, dbFile);
        } else {
            dbFile = new File(dbFile, DB_NAME);
            return dbFile.exists() && !dbFile.isDirectory() || copyDB(ctx, dbFile);
        }

    }

    private boolean copyDB(Context ctx, File dbFile) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(ctx.getAssets().open(DB_NAME));
            bos = new BufferedOutputStream(new FileOutputStream(new File(dbFile, DB_NAME)));
            byte[] b = new byte[1024];
            while (bis.read(b) != -1) {
                bos.write(b);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<City> getCityList(Context ctx) {
        List<City> cities = new ArrayList<>();
        File databasePath = ctx.getDatabasePath(DB_NAME);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath.getAbsolutePath(), null);
        Cursor query = sqLiteDatabase.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "pinyin desc");
        while (query.moveToNext()) {
            cities.add(new City(query.getString(query.getColumnIndex(COLUMN1)),
                    query.getString(query.getColumnIndex(COLUMN2))));
        }
        if (cities.size() > 0) {
            Collections.sort(cities);
        }
        query.close();
        sqLiteDatabase.close();
        return cities;
    }
}
