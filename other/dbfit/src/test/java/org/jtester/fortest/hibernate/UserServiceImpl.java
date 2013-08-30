package org.jtester.fortest.hibernate;

public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	private AddressService addressService;

	public AddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	public String findAddress() {
		return this.addressService.findAddress();
	}

	public User getUser(int id) {
		return this.getBeanById(id);
	}

	public void newUser(User user) {
		this.save(user);
	}
}
