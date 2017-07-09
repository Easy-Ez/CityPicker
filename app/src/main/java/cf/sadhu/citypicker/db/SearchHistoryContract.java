package cf.sadhu.citypicker.db;

import android.provider.BaseColumns;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:契约类
 */
public final class SearchHistoryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SearchHistoryContract() {
    }

    /* Inner class that defines the table contents */
    public static class SearchHistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "searchHistory";
        public static final String COLUMN_NAME_CITY_NAME = "name";
        public static final String COLUMN_NAME_PINYIN = "pinyin";
        public static final String COLUMN_NAME_OP_TIME = "time";
    }
}
