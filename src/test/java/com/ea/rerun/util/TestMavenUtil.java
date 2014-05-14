package com.ea.rerun.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class TestMavenUtil {
	@Test
	public void testRun() {
		MavenUtil
				.run(" mvn -B -f D:\\Users\\jinhuang\\EASAP\\nucleus\\NNG\\rating-integration\\pom.xml -U clean test -P BVT -Dtest.universe=onebox -Dtest=com.ea.eadp.rating.integration.testcase.cartRating.TestShippingRating#testScenario_shippingPromotionNotInEffectiveDate");
		
	}

	@Test
	public void testRunList() {
		List<String> list = new ArrayList<String>();
		list.add("mvn -B -f D:\\Users\\jinhuang\\EASAP\\nucleus\\NNG\\rating-integration\\pom.xml -U clean test -P BVT -Dtest.universe=onebox -Dtest=com.ea.eadp.rating.integration.testcase.cartRating.TestShippingRating#testScenario_shippingPromotionNotInEffectiveDate");
		//list.add("mvn -B -f D:\\Users\\jinhuang\\EASAP\\nucleus\\NNG\\rating-integration\\pom.xml clean test -P BVT -Dtest.universe=onebox -Dtest=com.ea.nucleus.preferences.emailaddresses.TestEmailAddressPostAndGet#testNotificationOnPost");
		MavenUtil.run(list);
	}

}
