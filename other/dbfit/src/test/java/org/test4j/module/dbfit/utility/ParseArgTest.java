package org.test4j.module.dbfit.utility;

import java.util.List;
import java.util.Map;

import org.test4j.module.dbfit.utility.ParseArg;
import org.test4j.module.dbfit.utility.SymbolUtil;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class ParseArgTest extends Test4J {

	@Test
	public void testParseMap() {
		String value = "key1:aaaa;key2:dddd";
		Map<String, String> map = ParseArg.parseMap(value);
		want.map(map).hasEntry("key1", "aaaa", "key2", "dddd");
	}

	@Test
	public void testParseList() {
		String value = "asdf;adfas;adfa";
		List<String> list = ParseArg.parseList(value);
		want.collection(list).sizeEq(3);
	}

	@Test
	public void testExactCellSymbolText() {
		String text = "adfd@{name} d";
		SymbolUtil.setSymbol("name", "my name");
		String result = ParseArg.exactCellSymbolText(text);
		want.string(result).contains("my name");
	}

	@Test
	public void containSymbols() {
		String text = "aaa@{name}ddd";
		boolean matched = ParseArg.containSymbols(text);
		want.bool(matched).isEqualTo(true);
	}
}
