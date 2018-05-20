package cn.edu.msaweatherdataserver.service;


import cn.edu.msaweatherdataserver.vo.WeatherResponse;

/**
 * Weather Data Service.
 * 
 * @since 1.0.0 2017年11月26日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface WeatherDataService {
	/**
	 * 根据城市ID查询天气数据
	 * 
	 * @param cityId
	 * @return
	 */
	WeatherResponse getDataByCityId(String cityId);

	/**
	 * 根据城市名称查询天气数据
	 * 
	 * @return
	 */
	WeatherResponse getDataByCityName(String cityName);
	
}
