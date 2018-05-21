package cn.edu.microweatherconfigclient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MicroWeatherConfigClientApplicationTests {


	@Value("${auther}")
	private String auther;

	@Test
	public void contextLoads() {
		System.out.println("=========="+auther);
		assertEquals("waylau.com", auther);
	}
}