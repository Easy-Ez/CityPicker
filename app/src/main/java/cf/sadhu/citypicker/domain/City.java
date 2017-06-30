package cf.sadhu.citypicker.domain;

import android.support.annotation.NonNull;

/**
 * Created by sadhu on 2017/6/29.
 * 描述: 城市实体
 */
public class City implements Comparable<City> {
    public String name;
    public String pinYin;

    public City(String name, String pinYin) {
        this.name = name;
        this.pinYin = pinYin;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", pinYin='" + pinYin + '\'' +
                '}';
    }

    public char getFirstChar() {
        return pinYin.charAt(0);
    }

    @Override
    public int compareTo(@NonNull City o) {
        return this.pinYin.compareTo(o.pinYin);
    }
}
