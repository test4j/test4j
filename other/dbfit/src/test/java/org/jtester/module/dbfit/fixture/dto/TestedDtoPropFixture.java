package org.jtester.module.dbfit.fixture.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jtester.module.ICore;
import org.jtester.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.jtester.module.dbfit.utility.ParseArg;

public class TestedDtoPropFixture extends DtoPropertyFixture implements ICore {
	private final static Logger log4j = Logger.getLogger(TestedDtoPropFixture.class);

	public void testMethod_String(String value) {
		want.string(value).start("a");
		log4j.info("test string");
	}

	public void testMethod_Integer(int value) {
		want.number(value).isEqualTo(3);
	}

	public void testMethod_dto(MyDto dto) {
		want.string(dto.firstName).contains("a");
		want.number(dto.age).isEqualTo(34);
		want.array(dto.getAddresses()).sizeEq(2);
		want.map(dto.map).hasKeys("key1", "key2");
		want.collection(dto.myList).sizeGe(2);
	}

	public void testMethod_dto_implicateParaClazz(MyDto dto) {
		want.string(dto.firstName).contains("a");
		want.number(dto.age).isEqualTo(34);
		want.array(dto.getAddresses()).sizeEq(2);
		want.map(dto.map).hasKeys("key1", "key2");
		want.collection(dto.myList).sizeGe(2);
	}

	public void testMethod_SingleValue(Map<String, String> map) {
		want.map(map).hasKeys("key1", "key2");
	}

	public static Map<String, String> parseMyMap(String str) {
		return parseMap(str);
	}

	public static class MyDto {
		String firstName;
		int age;
		private String[] addresses;
		Date date;
		Map<String, String> map = new HashMap<String, String>();

		private List<String> myList;

		public void parseMap(String value) {
			this.map = ParseArg.parseMap(value);
		}

		public void parseMyList(String value) {
			this.myList = parseList(value);
		}

		public String[] getAddresses() {
			return addresses;
		}
	}
}
