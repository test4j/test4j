package org.jtester.fortest.reflector;

import java.util.Date;

class PackagePrivateObject {

	final String name;

	public PackagePrivateObject(String name) {
		this.name = name;
	}

	public boolean isItYou() {
		return true;
	}

	public Object createInner() {
		return new Inner(new Date());
	}

	private class Inner {

		private final Date date;

		private Inner(Date date) {
			this.date = date;
		}

		@SuppressWarnings("unused")
		private Date getDate() {
			return date;
		}

		@Override
		public String toString() {
			return name + date;
		}

		@SuppressWarnings("unused")
		private class Nested {
		}
	}
}
