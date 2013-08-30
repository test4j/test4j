package org.jtester.junit.filter.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jtester.junit.annotations.AnnotationDefaultValue;
import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.filter.SuiteType;
import org.jtester.tools.commons.StringHelper;

/**
 * 在classpath中查找测试的过滤条件
 * 
 * @author darui.wudr
 * 
 */
public class FilterCondiction {

	private List<SuiteType> suiteTypes;
	/**
	 * searchInJars Specify if you want to include jar files in the search
	 */
	private boolean searchInJars;

	private List<String> positiveFilters;
	private List<String> negationFilters;
	private Class<?>[] includedBaseTypes;
	private Class<?>[] excludedBaseTypes;

	public FilterCondiction() {
		this.searchInJars = AnnotationDefaultValue.DEFAULT_INCLUDE_JARS;
		this.suiteTypes = Arrays.asList(AnnotationDefaultValue.DEFAULT_SUITE_TYPES);
		this.includedBaseTypes = AnnotationDefaultValue.DEFAULT_INCLUDED_BASE_TYPES;
		this.excludedBaseTypes = AnnotationDefaultValue.DEFAULT_EXCLUDED_BASES_TYPES;
		this.setFilterPatterns(AnnotationDefaultValue.DEFAULT_CLASSNAME_FILTERS);
	}

	public FilterCondiction(boolean searchInJar, String[] patterns, SuiteType[] suiteTypes, Class<?>[] baseTypes,
			Class<?>[] excludedBaseTypes) {
		this.searchInJars = searchInJar;
		this.suiteTypes = Arrays.asList(suiteTypes);
		this.includedBaseTypes = baseTypes;
		this.excludedBaseTypes = excludedBaseTypes;
		this.setFilterPatterns(patterns);
	}

	public boolean isSearchInJars() {
		return searchInJars;
	}

	public List<SuiteType> getSuiteTypes() {
		return suiteTypes;
	}

	public List<String> getPositiveFilters() {
		return positiveFilters;
	}

	public List<String> getNegationFilters() {
		return negationFilters;
	}

	public Class<?>[] getIncludedBaseTypes() {
		return includedBaseTypes;
	}

	public Class<?>[] getExcludedBaseTypes() {
		return excludedBaseTypes;
	}

	/**
	 * filterPatterns A set of regex expression to specify the class names to
	 * include (included if any pattern matches); use null to include all test
	 * classes in all packages.
	 * 
	 * @param filterPatterns
	 */
	private void setFilterPatterns(String[] filterPatterns) {
		this.positiveFilters = new ArrayList<String>();
		this.negationFilters = new ArrayList<String>();
		if (filterPatterns == null) {
			return;
		}
		for (String pattern : filterPatterns) {
			String _pattern = StringHelper.trim(pattern);
			if (StringHelper.isBlankOrNull(_pattern)) {
				continue;
			}
			if (_pattern.startsWith("!")) {
				_pattern = _pattern.substring(1).trim();
				if (StringHelper.isBlankOrNull(_pattern)) {
					continue;
				}
				String regex = convertToRegular(_pattern);
				negationFilters.add(regex);
			} else {
				String regex = convertToRegular(_pattern);
				positiveFilters.add(regex);
			}
		}
	}

	private String convertToRegular(String input) {
		if (StringHelper.isBlankOrNull(input)) {
			return ".*";
		} else {
			String regex = input.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");
			return regex;
		}
	}

	public void initFilters(ClazFinder testFilterAnnotation) {
		if (testFilterAnnotation == null) {
			return;
		}
		this.searchInJars = testFilterAnnotation.inJars();
		this.suiteTypes = Arrays.asList(testFilterAnnotation.value());
		this.includedBaseTypes = testFilterAnnotation.baseType().includes();
		this.excludedBaseTypes = testFilterAnnotation.baseType().excludes();
		String[] patterns = testFilterAnnotation.patterns();
		this.setFilterPatterns(patterns);
	}
}
