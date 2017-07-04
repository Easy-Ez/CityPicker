package cf.sadhu.citypicker.util;

import android.content.Context;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

/**
 * Created by sadhu on 2017/7/4.
 * 描述
 */
public class PinyinUtils {
    public static PinyinUtils instance;

    public static PinyinUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PinyinUtils(context);
        }
        return instance;
    }

    private PinyinUtils(Context context) {
        // 添加中文城市词典
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(context)));
    }

    public String toPinyin(String s) {
        return Pinyin.toPinyin(s, "");
    }

}
