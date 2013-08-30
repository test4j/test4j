package org.jtester.module.dbfit.fixture.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jtester.module.dbfit.utility.ParseArg;
import org.jtester.tools.reflector.FieldAccessor;

import fit.Fixture;
import fit.Parse;
import fit.exception.FitFailureException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DtoCheckFixture extends SpringFixture {
	private static ThreadLocal<Map<String, Object>> localDto = new ThreadLocal<Map<String, Object>>();
	private String bindings[];

	Object dto;
	String storekey;
	/**
	 * 数据比较的当前行，如果是单一对象则index=0
	 */
	int index;
	/**
	 * 表格处理的当前行
	 */
	Parse currentRow = null;

	public void doRows(Parse rows) {
		if (rows == null || rows.parts == null) {
			throw new FitFailureException("You must specify identified name of stored dto.");
		}
		dto = findDto(rows.parts);
		index = 0;
		Parse rest = rows.more;
		if (rest == null || rest.parts == null) {
			throw new RuntimeException("You must specify checked proproties!");
		}
		bindDtoProperties(rest.parts);
		currentRow = rest;
		super.doRows(rest.more);

		addSurplusRows();
	}

	private void addSurplusRows() {
		if (currentRow == null) {
			return;
		}
		if (dto instanceof List) {
			List list = (List) dto;
			for (int loop = index; loop < list.size(); loop++) {
				Object moreObject = list.get(loop);
				addSurplusRow(moreObject);
			}
		} else if (index == 0) {
			addSurplusRow(dto);
		}
	}

	private void addSurplusRow(Object obj) {
		Parse newRow = new Parse("tr", null, null, null);
		currentRow.more = newRow;
		currentRow = newRow;
		try {
			String value = getValueFrom(obj, bindings[0]);
			Parse firstCell = new Parse("td", value, null, null);
			newRow.parts = firstCell;
			firstCell.addToBody(Fixture.gray(" surplus"));
			wrong(firstCell);
			for (int i = 1; i < bindings.length; i++) {
				Parse nextCell = new Parse("td", getValueFrom(obj, bindings[i]), null, null);
				firstCell.more = nextCell;
				firstCell = nextCell;
			}
		} catch (Throwable e) {
			exception(newRow, e);
		}
	}

	@Override
	public void doRow(Parse row) {
		this.currentRow = row;
		Parse cells = row.parts;
		try {
			Object expected = getObjectByIndex();
			index++;
			for (int i = 0; cells != null && i < bindings.length; i++, cells = cells.more) {
				String binding = bindings[i];
				String text = ParseArg.parseCellValue(cells);
				String value = getValueFrom(expected, binding);
				if (text == null && value == null || text != null && text.equals(value)) {
					this.right(cells);
				} else {
					String message = String.format("expected value %s,but actual value is %s", text, value);
					this.exception(cells, new RuntimeException(message));
					return;
				}
			}
		} catch (Throwable e) {
			this.exception(cells, e);
		}
	}

	private Object getObjectByIndex() {
		if (dto == null) {
			if (index == 0) {
				return null;
			} else {
				throw new RuntimeException("no more expected data, actual data size is null");
			}
		}
		if (dto instanceof List) {
			List list = (List) dto;
			if (index < list.size()) {
				return ((List) dto).get(index);
			} else {
				throw new RuntimeException("no more expected data, actual data size is:" + list.size());
			}
		} else {
			return dto;
		}
	}

	/**
	 * 存储要比较的对象<br>
	 * 集合和数组类型转换为list<br>
	 * 
	 * @param name
	 * @param dto
	 */
	public static void storeDto(final String name, final Object dto) {
		String key = camel(name);
		Map<String, Object> map = localDto.get();
		if (map == null) {
			map = new ConcurrentHashMap<String, Object>();
			localDto.set(map);
		}
		Object store = dto;
		if (dto instanceof Collection && !(dto instanceof List)) {
			store = new ArrayList((Collection) dto);
		} else if (dto instanceof Object[]) {
			List<Object> list = new ArrayList<Object>();
			Object[] os = (Object[]) dto;
			for (Object o : os) {
				list.add(o);
			}
			store = list;
		}
		map.put(key, store);
	}

	/**
	 * 移除存储的对象
	 * 
	 * @param name
	 */
	public static void removeDto(final String name) {
		Map<String, Object> map = localDto.get();
		if (map != null) {
			map.remove(name);
		}
	}

	private Object findDto(Parse dtoParse) {
		String storekey = camel(dtoParse.text());
		Map<String, Object> maps = localDto.get();
		if (maps == null || maps.containsKey(storekey) == false) {
			throw new RuntimeException("can't find stored data for key[" + storekey + "]");
		}
		this.right(dtoParse);
		return maps.get(storekey);
	}

	/**
	 * 绑定参数的字段名称
	 * 
	 * @param dtoProps
	 */
	private void bindDtoProperties(Parse dtoProps) {
		int size = dtoProps.size();
		try {
			this.bindings = new String[size];
			for (int i = 0; dtoProps != null; i++, dtoProps = dtoProps.more) {
				String property = camel(dtoProps.text());
				bindings[i] = property;
			}
		} catch (Throwable e) {
			this.wrong(dtoProps);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得对应字段的值，如果是map，则返回对应key值
	 * 
	 * @param expected
	 * @param field
	 * @return
	 */
	private static String getValueFrom(Object expected, String field) {
		if (expected == null) {
			return null;
		}
		Object value = null;
		if (expected instanceof Map) {
			value = ((Map) expected).get(field);
		} else {
			FieldAccessor accessor = new FieldAccessor(expected, field);
			value = accessor.get(expected);
		}
		if (value == null) {
			return "<null>";
		} else {
			return value.toString();
		}
	}
}
