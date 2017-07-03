package cf.sadhu.citypicker.domain;

/**
 * Created by sadhu on 2017/6/29.
 * 描述: 城市实体
 */
public class City implements /*Comparable<City> ,*/ICity {
    private String name;
    private String pinYin;

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

    @Override
    public char getFirstChar() {
        return pinYin.charAt(0);
    }

    /*@Override
    public int compareTo(@NonNull City o) {
        return this.pinYin.compareTo(o.pinYin);
    }*/

    @Override
    public String getCityName() {
        return name;
    }

    @Override
    public String getCityPinYin() {
        return pinYin;
    }
}
