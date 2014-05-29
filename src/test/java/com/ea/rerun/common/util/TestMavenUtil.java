package com.ea.rerun.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ea.rerun.common.util.MavenUtil;

public class TestMavenUtil {
	@Test
	public void testRun() {
		String str = "mvn -B -F D:\\ea\\nucleus\\MAIN\\commerce-integration\\pom.xml -U clean test -P Comprehensive -Dtest.universe=onebox -Dtest=com.ea.fusion.commerce.cart.checkout.TestCartCheckoutCreditCard#testCreditCardPackageGoodsReturnPartialReturnCartCheckout";
		MavenUtil
				.run(str);
		
	}

	@Test
	public void testRunList() {
		List<String> list = new ArrayList<String>();
		list.add("mvn -B -f D:\\Users\\jinhuang\\EASAP\\nucleus\\NNG\\rating-integration\\pom.xml -U clean test -P BVT -Dtest.universe=onebox -Dtest=com.ea.eadp.rating.integration.testcase.cartRating.TestShippingRating#testScenario_shippingPromotionNotInEffectiveDate");
		//list.add("mvn -B -f D:\\Users\\jin    huang\\EASAP\\nucleus\\NNG\\rating-integration\\pom.xml clean test -P BVT -Dtest.universe=onebox -Dtest=com.ea.nucleus.preferences.emailaddresses.TestEmailAddressPostAndGet#testNotificationOnPost");
		MavenUtil.run(list);
	}

}
