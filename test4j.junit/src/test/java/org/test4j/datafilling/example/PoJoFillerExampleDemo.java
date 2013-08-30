package org.test4j.datafilling.example;

import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateAddress;
import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateArticle;
import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateBankAccount;
import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateCountry;
import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateOrder;
import static org.test4j.datafilling.example.PoJoFillerExampleDemoHelper.validateOrderItem;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.test4j.datafilling.Filler;
import org.test4j.datafilling.common.FillingConstants;
import org.test4j.datafilling.model.example.Address;
import org.test4j.datafilling.model.example.Article;
import org.test4j.datafilling.model.example.BankAccount;
import org.test4j.datafilling.model.example.Client;
import org.test4j.datafilling.model.example.Country;
import org.test4j.datafilling.model.example.Order;
import org.test4j.datafilling.model.example.OrderItem;
import org.test4j.module.ICore;

public class PoJoFillerExampleDemo implements ICore {

	@Test
	public void testCountrySetup() {
		Country pojo = Filler.filling(Country.class);
		validateCountry(pojo);
	}

	@Test
	public void testArticleSetup() {
		Article pojo = Filler.filling(Article.class);
		validateArticle(pojo);
	}

	@Test
	public void testOrderItemSetup() {
		OrderItem pojo = Filler.filling(OrderItem.class);
		validateOrderItem(pojo);
	}

	@Test
	public void testOrderSetup() {
		Order pojo = Filler.filling(Order.class);
		validateOrder(pojo);
	}

	@Test
	public void testAddressSetup() {
		Address pojo = Filler.filling(Address.class);
		validateAddress(pojo);
	}

	@Test
	public void testBankAccountSetup() {
		BankAccount pojo = Filler.filling(BankAccount.class);
		validateBankAccount(pojo);
	}

	@Test
	public void testClientSetup() {
		Client pojo = Filler.filling(Client.class);
		Assert.assertNotNull("The pojo cannot be null!", pojo);

		Assert.assertNotNull("The client's first name cannot be null!", pojo.getFirstName());
		Assert.assertTrue("The client's first name cannot be empty!", pojo.getFirstName().length() > 0);

		String expectedFirstName = "Michael";
		Assert.assertEquals("The client's first name is not " + expectedFirstName, expectedFirstName,
				pojo.getFirstName());

		Assert.assertNotNull("The client's last name cannot be null!", pojo.getLastName());
		Assert.assertTrue("The client's last name cannot be empty!", pojo.getLastName().length() > 0);

		Assert.assertNotNull("The date created cannot be null!", pojo.getDateCreated());

		List<Order> orders = pojo.getOrders();
		Assert.assertNotNull("The orders cannot be null!", orders);
		int expectedOrdersNbr = 3;
		Assert.assertTrue("The expected number of orders is " + expectedOrdersNbr, orders.size() == expectedOrdersNbr);

		for (Order order : orders) {
			validateOrder(order);
		}

		List<Address> addresses = pojo.getAddresses();
		Assert.assertNotNull("The addresses cannot be null!", addresses);
		int expectedAddressesNbr = 2;
		Assert.assertTrue("The expected number of addresses is " + expectedAddressesNbr,
				addresses.size() == expectedAddressesNbr);

		for (Address address : addresses) {
			validateAddress(address);
		}

		List<BankAccount> bankAccounts = pojo.getBankAccounts();
		Assert.assertNotNull("The bank accounts cannot be null!", bankAccounts);
		int expectedBankAccountsNbr = FillingConstants.ARRAY_DEFAULT_SIZE;
		Assert.assertTrue("The expected number of addresses is " + expectedBankAccountsNbr,
				bankAccounts.size() == expectedBankAccountsNbr);

		for (BankAccount bankAccount : bankAccounts) {
			validateBankAccount(bankAccount);
		}
	}
}
