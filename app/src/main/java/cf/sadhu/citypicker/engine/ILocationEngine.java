package cf.sadhu.citypicker.engine;

import cf.sadhu.citypicker.domain.City;

/**
 * Created by sadhu on 2017/7/2.
 * 描述: 定位功能 抽象接口
 */
public interface ILocationEngine {
    City getLocationInfo();
}
