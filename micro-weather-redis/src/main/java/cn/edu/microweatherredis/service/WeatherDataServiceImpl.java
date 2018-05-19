package cn.edu.microweatherredis.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.edu.microweatherredis.vo.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * WeatherDataService 实现.
 * 
 * @since 1.0.0 2017年11月22日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Service
public class WeatherDataServiceImpl implements WeatherDataService {
	private final static Logger logger = LoggerFactory.getLogger(WeatherDataServiceImpl.class);  
	
	private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";

	private static final long TIME_OUT = 1800L; // 1800s
	
	@Autowired
	private RestTemplate restTemplate;

	//注入StringRedisTemplate
	//说明：在windows使用redis,运行redis跟路径下的redis-server.exe文件开启服务即可
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public cn.edu.microweatherredis.vo.WeatherResponse getDataByCityId(String cityId) {
		String uri = WEATHER_URI + "citykey=" + cityId;
		return this.doGetWeahter(uri);
	}

	@Override
	public  cn.edu.microweatherredis.vo.WeatherResponse getDataByCityName(String cityName) {
		String uri = WEATHER_URI + "city=" + cityName;
		return this.doGetWeahter(uri);
	}
	
	private  cn.edu.microweatherredis.vo.WeatherResponse doGetWeahter(String uri) {
		String key = uri;
		String strBody = null;
		ObjectMapper mapper = new ObjectMapper();
		WeatherResponse resp = null;
		ValueOperations<String, String>  ops = stringRedisTemplate.opsForValue();
		// 先查缓存，缓存有的取缓存中的数据
		if (stringRedisTemplate.hasKey(key)) {
			logger.info("Redis has data");
			strBody = ops.get(key);
		} else {
			logger.info("Redis don't has data");
			// 缓存没有，再调用服务接口来获取
	 		ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);

	 		if (respString.getStatusCodeValue() == 200) {
				strBody = respString.getBody();
			}
			
			// 数据写入缓存
			ops.set(key, strBody, TIME_OUT, TimeUnit.SECONDS);
		}

		try {
			resp = mapper.readValue(strBody,  cn.edu.microweatherredis.vo.WeatherResponse.class);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("Error!",e);
		}
		
		return resp;
	}

}
