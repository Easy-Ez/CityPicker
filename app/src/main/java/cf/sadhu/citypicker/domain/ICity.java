package cf.sadhu.citypicker.domain;

import java.io.Serializable;

/**
 * Created by sadhu on 2017/7/3.
 * 描述
 */
public interface ICity extends Serializable {
    String getCityName();

    String getCityPinYin();

    char getFirstChar();
}
