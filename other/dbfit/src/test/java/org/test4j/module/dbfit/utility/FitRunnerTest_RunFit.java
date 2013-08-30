package org.test4j.module.dbfit.utility;

import java.util.HashMap;
import java.util.Map;

import org.test4j.module.dbfit.fixture.dto.DtoPropertyFixture;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "test4j")
public class FitRunnerTest_RunFit extends Test4J {
	public void runFit() {
		FitRunner.runFit(FitRunnerTest_RunFit.class, "testMethod_String.wiki", "testMethod_Integer.wiki");
	}

	public void runFit_Symbols() {
		Map<String, String> symbols = new HashMap<String, String>();
		symbols.put("name", "my name");
		symbols.put("age", "34");
		FitRunner.runFit(FitRunnerTest_RunFit.class, symbols, "runFit_Symbols.wiki");
	}

	public static class SymbolPropertyFixture extends DtoPropertyFixture {
		public void checkDto(SymbolDto dto) {
			want.object(dto).propertyEq("name", "my name");
		}
	}

	public static class SymbolDto {
		String name;
		int age;
		boolean isMale;
	}
}
