package cf.sadhu.citypicker.db;

import java.util.List;

/**
 * Created by sadhu on 2017/7/9.
 * 描述:
 */
public interface Dao<T> {
    List<T> queryAll();

    boolean inseart(T t);
}
