package org.jtester.module.dbfit.fixture.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.module.dbfit.fixture.dto.DtoCheckFixture;
import org.jtester.module.dbfit.utility.FitRunner;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class DtoCheckFixtureTest extends JTester {

	public void qureyStoreData_moreThenData() {

		List<DtoChecked> list = new ArrayList<DtoChecked>();
		list.add(DtoChecked.mock("ddd", 24, 45454.3d, true));

		DtoCheckFixture.storeDto("myCheckedDto", list);
		try {
			FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData.wiki");
			want.fail();
		} catch (Throwable e) {
			String message = e.getMessage();
			want.string(message).contains("right=5").contains("wrong=0").contains("exceptions=1");
		}
		DtoCheckFixture.removeDto("myCheckedDto");
	}

	public void qureyStoreData() {
		List<DtoChecked> list = new ArrayList<DtoChecked>();
		list.add(DtoChecked.mock("ddd", 24, 45454.3d, true));
		list.add(DtoChecked.mock("eee", 43, 45.3d, false));

		DtoCheckFixture.storeDto("myCheckedDto", list);
		FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData.wiki");
		DtoCheckFixture.removeDto("myCheckedDto");
	}

	public void qureyStoreData_lessThen() {
		List<DtoChecked> list = new ArrayList<DtoChecked>();
		list.add(DtoChecked.mock("ddd", 24, 45454.3d, true));
		list.add(DtoChecked.mock("eee", 43, 45.3d, false));
		// more data
		list.add(DtoChecked.mock("ccc", 43, 45.3d, false));
		list.add(DtoChecked.mock("ddd", 43, 45.3d, false));

		DtoCheckFixture.storeDto("my checked dto", list);
		try {
			FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData.wiki");
			want.fail();
		} catch (Throwable e) {
			String message = e.getMessage();
			want.string(message).contains("right=9").contains("wrong=2").contains("exceptions=0");
		}
		DtoCheckFixture.removeDto("myCheckedDto");
	}

	public void qureyStoreData_SingleObject() {
		DtoChecked dto = DtoChecked.mock("my name", 34, 4444, true);
		DtoCheckFixture.storeDto("single dto", dto);
		FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData_SingleObject.wiki");
		DtoCheckFixture.removeDto("single dto");
	}

	public void qureyStoreData_map() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "my name");
		map.put("isMale", true);

		DtoCheckFixture.storeDto("single dto", map);
		FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData_SingleObject.wiki");
		DtoCheckFixture.removeDto("single dto");
	}

	public void qureyStoreData_map_usePara() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "my name");
		map.put("isMale", true);

		DtoCheckFixture.storeDto("single dto", map);

		Map<String, String> symbols = new HashMap<String, String>() {
			private static final long serialVersionUID = -4093650769126918203L;
			{
				put("name", "my name");
			}
		};
		FitRunner.runFit(DtoCheckFixtureTest.class, symbols, "dtoCheckFixture_usePara.wiki");
		DtoCheckFixture.removeDto("single dto");
	}

	public void qureyStoreData_array() {
		List<DtoChecked> list = new ArrayList<DtoChecked>();
		list.add(DtoChecked.mock("ddd", 24, 45454.3d, true));
		list.add(DtoChecked.mock("eee", 43, 45.3d, false));

		DtoCheckFixture.storeDto("my checked dto", list.toArray(new Object[0]));
		FitRunner.runFit(DtoCheckFixtureTest.class, "qureyStoreData.wiki");
		DtoCheckFixture.removeDto("my checked dto");
	}

	public static class DtoChecked {
		private String name;

		private int age;

		private double parary;

		private boolean isMale;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public double getParary() {
			return parary;
		}

		public void setParary(double parary) {
			this.parary = parary;
		}

		public boolean isMale() {
			return isMale;
		}

		public void setMale(boolean isMale) {
			this.isMale = isMale;
		}

		public static DtoChecked mock(String name, int age, double parary, boolean isMale) {
			DtoChecked dto = new DtoChecked();
			dto.age = age;
			dto.parary = parary;
			dto.name = name;
			dto.isMale = isMale;
			return dto;
		}
	}
}
