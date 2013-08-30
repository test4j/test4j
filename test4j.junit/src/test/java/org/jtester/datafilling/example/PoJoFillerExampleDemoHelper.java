package org.jtester.datafilling.example;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.jtester.datafilling.model.example.Address;
import org.jtester.datafilling.model.example.Article;
import org.jtester.datafilling.model.example.BankAccount;
import org.jtester.datafilling.model.example.Country;
import org.jtester.datafilling.model.example.Order;
import org.jtester.datafilling.model.example.OrderItem;
import org.jtester.module.ICore;

public class PoJoFillerExampleDemoHelper implements ICore {

	public static void validateBankAccount(BankAccount pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");

		Assert.assertTrue("The bank account cannot be zero!", pojo.getAccount() != 0);
		Assert.assertTrue("The account balance cannot be zero!", pojo.getBalance() != 0.0);
		Assert.assertNotNull("The bank name cannot be null!", pojo.getBank());
		Assert.assertTrue("The bank name cannot be empty!", pojo.getBank().length() > 0);
		Assert.assertNotNull("The sort code cannot be null!", pojo.getSortCode());
		Assert.assertTrue("The sort code cannot be empty!", pojo.getSortCode().length() > 0);
	}

	public static void validateAddress(Address pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");
		Assert.assertNotNull("The address1 cannot be null!", pojo.getAddress1());
		Assert.assertNotNull("The address2 cannot be null!", pojo.getAddress2());
		Assert.assertNotNull("The city cannot be null!", pojo.getCity());
		Assert.assertNotNull("The zipCode cannot be null!", pojo.getZipCode());

		validateCountry(pojo.getCountry());
	}

	public static void validateOrder(Order pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");

		Assert.assertTrue("The order id must not be zero!", pojo.getId() != 0);
		Calendar createDate = pojo.getCreateDate();
		Assert.assertNotNull("The create date must not be null!", createDate);

		Assert.assertTrue("The order total amount must not be zero!", pojo.getTotalAmount() != 0.0);

		List<OrderItem> orderItems = pojo.getOrderItems();
		Assert.assertNotNull("The order items must not be null!", orderItems);
		Assert.assertFalse("The order items must not be empty!", orderItems.isEmpty());

		int expectedNbrElements = 5;

		Assert.assertTrue("The expected number of elements " + expectedNbrElements
				+ " does not match the actual number: " + orderItems.size(), orderItems.size() == expectedNbrElements);

		for (OrderItem orderItem : orderItems) {
			validateOrderItem(orderItem);
		}
	}

	public static void validateOrderItem(OrderItem pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");

		Assert.assertTrue("The order item id cannot be zero!", pojo.getId() != 0);
		Assert.assertTrue("The order item line amount cannot be zero!", pojo.getLineAmount() != 0.0);
		Assert.assertNull("The Order Item note must be null because of @PodamExclude annotation", pojo.getNote());
		validateArticle(pojo.getArticle());
	}

	public static void validateArticle(Article pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");

		int expectedMaxValue = 100000;

		Assert.assertTrue("The article id max value must not exceed " + expectedMaxValue,
				pojo.getId() <= expectedMaxValue);
		Assert.assertNotNull("The item cost cannot be null!", pojo.getItemCost());
		Assert.assertTrue("The item cost must have a value different from zero!", pojo.getItemCost() != 0);
		Assert.assertNotNull("The article description cannot be null!", pojo.getDescription());
	}

	public static void validateCountry(Country pojo) {
		want.object(pojo).notNull("The pojo cannot be null!");

		String countryCode = pojo.getCountryCode();
		Assert.assertNotNull("The country Code cannot be null!", countryCode);
		int countryCodeLength = 2;
		Assert.assertTrue(
				"The length of the country code must be " + countryCodeLength + "! but was " + countryCode.length(),
				countryCode.length() == countryCodeLength);

		Assert.assertTrue("country id must be different from zero!", pojo.getCountryId() != 0);
		Assert.assertNotNull("The country description must not be null!", pojo.getDescription());
		Assert.assertTrue("The country description must not be empty!", pojo.getDescription().length() > 0);
	}
}
