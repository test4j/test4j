package org.jtester.junit.filter.finder;

import java.util.List;

import mockit.Mock;

import org.jtester.junit.annotations.DataFrom;
import org.jtester.module.ICore;
import org.junit.Test;

public class FilterCondictionTest implements ICore {

	@Test
	@DataFrom("dataForFilterPatterns")
	public void testSetFilterPatterns(String[] patterns, String[] positionPatterns, String[] negationPatterns)
			throws Exception {
		FilterCondiction filter = reflector.newInstance(FilterCondiction.class);
		new MockUp<FilterCondiction>() {
			/**
			 * 这里不测试正则表达式的转换，表达式转换另测
			 * 
			 * @param input
			 * @return
			 */
			@Mock
			public String convertToRegular(String input) {
				return input;
			}
		};
		reflector.invoke(filter, "setFilterPatterns", new Object[] { patterns });
		List<String> position = filter.getPositiveFilters();
		List<String> negation = filter.getNegationFilters();
		want.list(position).reflectionEq(positionPatterns);
		want.list(negation).reflectionEq(negationPatterns);
	}

	public static DataIterator dataForFilterPatterns() {
		return new DataIterator() {
			{
				data(null, new String[] {}, new String[] {});
				data(new String[] { "a", "!b" }, new String[] { "a" }, new String[] { "b" });
				data(new String[] { " ", " ! " }, new String[] {}, new String[] {});
				data(new String[] { "  a  ", "  !  b  " }, new String[] { "a" }, new String[] { "b" });
				data(new String[] { null, "!c.b.a", "a.b.c", "     " }, new String[] { "a.b.c" },
						new String[] { "c.b.a" });
			}
		};
	}

	@Test
	@DataFrom("dataForConvertToRegular")
	public void testConvertToRegular(String input, String pattern) throws Exception {
		FilterCondiction filter = reflector.newInstance(FilterCondiction.class);
		String result = reflector.invoke(filter, "convertToRegular", new Object[] { input });
		want.string(result).isEqualTo(pattern);
	}

	public static DataIterator dataForConvertToRegular() {
		return new DataIterator() {
			{
				data("", ".*");
				data(null, ".*");
				data("a.b.c", "a\\.b\\.c");
				data("a*", "a.*");
			}
		};
	}

	@Test
	public void testFilterPattern() throws Exception {
		boolean matched = "a.b.cTest".matches("\\..*Test");
		want.bool(matched).isEqualTo(false);

		matched = "a.b.cTest".matches("b");
		want.bool(matched).isEqualTo(false);

		matched = "ppp.MyTest".matches("\\..*Test");
		want.bool(matched).isEqualTo(false);
	}
}
