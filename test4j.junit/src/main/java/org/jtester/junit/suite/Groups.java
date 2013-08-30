package org.jtester.junit.suite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtester.junit.annotations.AnnotationDefaultValue;
import org.jtester.junit.annotations.Group;
import org.jtester.junit.annotations.RunGroup;
import org.jtester.tools.commons.StringHelper;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class Groups extends Suite {

	public Groups(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
		try {
			RunGroup annotation = klass.getAnnotation(RunGroup.class);
			String[] includedGroups = annotation == null ? AnnotationDefaultValue.DEFAULT_GROUP_VALUE : annotation
					.includes();
			String[] excludedGroups = annotation == null ? AnnotationDefaultValue.DEFAULT_GROUP_VALUE : annotation
					.excludes();
			Filter filter = new GroupFilter(includedGroups, excludedGroups);
			filter(filter);
		} catch (NoTestsRemainException e) {
			throw new InitializationError(e);
		}
		assertNoCategorizedDescendentsOfUncategorizeableParents(getDescription());
	}

	private void assertNoCategorizedDescendentsOfUncategorizeableParents(Description description)
			throws InitializationError {
		if (!canHaveCategorizedChildren(description))
			assertNoDescendantsHaveCategoryAnnotations(description);
		for (Description each : description.getChildren())
			assertNoCategorizedDescendentsOfUncategorizeableParents(each);
	}

	private void assertNoDescendantsHaveCategoryAnnotations(Description description) throws InitializationError {
		for (Description each : description.getChildren()) {
			if (each.getAnnotation(Category.class) != null)
				throw new InitializationError(
						"Category annotations on Parameterized classes are not supported on individual methods.");
			assertNoDescendantsHaveCategoryAnnotations(each);
		}
	}

	// If children have names like [0], our current magical category code can't
	// determine their
	// parentage.
	private static boolean canHaveCategorizedChildren(Description description) {
		for (Description each : description.getChildren())
			if (each.getTestClass() == null)
				return false;
		return true;
	}

	public static class GroupFilter extends Filter {
		public static GroupFilter include(String[] includedGroups) {
			return new GroupFilter(includedGroups, AnnotationDefaultValue.DEFAULT_GROUP_VALUE);
		}

		private final List<String> gIncluded;

		private final List<String> gExcluded;

		public GroupFilter(String[] includedGroups, String[] excludedGroups) {
			gIncluded = Arrays.asList(includedGroups);
			gExcluded = Arrays.asList(excludedGroups);
		}

		@Override
		public String describe() {
			return "groups " + StringHelper.merger(gIncluded, ',');
		}

		@Override
		public boolean shouldRun(Description description) {
			if (hasCorrectGroupAnnotation(description)) {
				return true;
			}
			for (Description each : description.getChildren()) {
				if (shouldRun(each)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 测试对象是否包含包含在执行组中，或者是被排除运行
		 * 
		 * @param description
		 * @return
		 */
		private boolean hasCorrectGroupAnnotation(Description description) {
			Set<String> groups = groups(description);
			if (groups.isEmpty()) {
				return gIncluded.size() == 0;
			}
			for (String group : groups) {
				if (gExcluded.contains(group)) {
					return false;
				}
			}
			if (gIncluded.size() == 0) {
				return true;
			}
			for (String group : groups) {
				if (gIncluded.contains(group)) {
					return true;
				}
			}
			return false;
		}

		private Set<String> groups(Description description) {
			Set<String> groups = new HashSet<String>();
			String[] directGroups = directGroup(description);
			groups.addAll(Arrays.asList(directGroups));
			String[] parentGroups = directGroup(parentDescription(description));
			groups.addAll(Arrays.asList(parentGroups));
			return groups;
		}

		private Description parentDescription(Description description) {
			Class<?> testClass = description.getTestClass();
			if (testClass == null)
				return null;
			Description parentDescription = Description.createSuiteDescription(testClass);
			return parentDescription;
		}

		/**
		 * 直接在测试上标记的分组信息
		 * 
		 * @param description
		 * @return
		 */
		private String[] directGroup(Description description) {
			if (description == null) {
				return AnnotationDefaultValue.DEFAULT_GROUP_VALUE;
			}
			Group annotation = description.getAnnotation(Group.class);
			if (annotation == null) {
				return AnnotationDefaultValue.DEFAULT_GROUP_VALUE;
			}
			return annotation.value();
		}
	}
}
