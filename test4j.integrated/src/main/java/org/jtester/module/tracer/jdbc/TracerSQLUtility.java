package org.jtester.module.tracer.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jtester.tools.commons.StringHelper;

/**
 * SQL语法高亮显示器
 */
public class TracerSQLUtility {
	// 关键字
	public static final String[] SQL_KEY_WORDS = { "select", "from", "update", "delete", "insert", "into", "where",
			"group", "by", "having", "values", "and", "or", "(", ")", "as", "like", "not", "in", "exists", "order",
			"asc", "desc", "on", "join", "outer", "set", "create", "table", "alter", "drop", "index" };

	// 关键字替换前缀
	private static String keywordLightPrefix = "<font color='#000080'><b>";

	// 关键字替换后缀
	private static String keywordLightSuffix = "</b></font>";

	// 表名替换前缀
	private static String tablesLightPrefix = "<font color='#3F7F5F'><b>";

	// 表名替换后缀
	private static String tablesLightSuffix = "</b></font>";

	/**
	 * 分析出表名列表
	 * 
	 * @param sql
	 * @return
	 */
	public static Collection<String> analyseSqlTables(String sql) {
		// 词法分析
		String[] words = splitWord(sql, null);
		Collection<String> tables = new ArrayList<String>();
		// 语法分析
		tableAnalyse(new Counter(), words, tables);

		return tables;
	}

	/**
	 * 高亮关键字
	 * 
	 * @param sql
	 * @return
	 */
	public static String highlightKeywords(String sql) {
		String lightSql = replaceSql(sql, null, true);
		return lightSql;
	}

	/**
	 * 高亮sql,包括关键字和表名
	 * 
	 * @param sql
	 * @return
	 */
	public static String highlightSql(String sql) {
		Collection<String> tables = analyseSqlTables(sql);

		String lightSql = replaceSql(sql, tables, true);
		return lightSql;
	}

	/**
	 * 完成sql替换,包括
	 * 
	 * @return
	 */
	private static String replaceSql(String sql, final Collection<String> tables, final boolean replaceKeyword) {
		// 替换必须保证在一次词法扫描完成，否则，替换的内容会互相影响
		// @ TODO 这里splitWord还不能做到真正的一次扫描，只是模拟一次词法分析

		final StringBuffer replacement = new StringBuffer(" ");

		// 词法分析处理
		LexicalAnalysisProcessor processor = new LexicalAnalysisProcessor() {

			public void process(String word) {
				int placeType = 0;

				if (replaceKeyword) {
					for (int i = 0; i < SQL_KEY_WORDS.length; i++) {
						if (SQL_KEY_WORDS[i].equals(word.toLowerCase())) {
							placeType = 1;
							break;// 表名的优先级大于关键字，所以这里不return
						}
					}
				}

				if (tables != null && tables.contains(word)) {
					placeType = 2;
				}

				switch (placeType) {
				case 0:// 普通词汇
					replacement.append(word + " ");
					break;
				case 1:// 关键字
					replacement.append(keywordLightPrefix + word + keywordLightSuffix + " ");
					break;
				case 2:// 表名
					replacement.append(tablesLightPrefix + word + tablesLightSuffix + " ");
					break;
				default:
				}
			}

		};

		splitWord(sql, processor);

		return replacement.toString();
	}

	private static class Counter {
		int num = 0;
	}

	/**
	 * 对表名的语法分析
	 * 
	 * @param words
	 * @return
	 */
	private static void tableAnalyse(Counter i, String[] words, Collection<String> tables) {
		for (; i.num < words.length; i.num++) {
			// 如果遇到'('跳入下一个子句语法分析
			if ("(".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				tableAnalyse(i, words, tables);
				continue;
			}
			// 如果遇到')'退出当前子句语法分析
			if (")".equals(words[i.num].trim().toLowerCase())) {
				return;
			}

			// 如果遇到from
			if ("from".equals(words[i.num].trim().toLowerCase())) {
				for (i.num++; i.num < words.length; i.num++) {
					String fromNext = words[i.num];
					// 如果遇到'('跳入下一个子句语法分析
					if ("(".equals(fromNext.trim().toLowerCase())) {
						i.num++;
						tableAnalyse(i, words, tables);
						continue;
					}
					// 如果遇到')'退出当前子句语法分析
					if (")".equals(fromNext.trim().toLowerCase())) {
						return;
					}
					// 如果遇到'where,into'from表达式结束
					if ("where".equals(fromNext.trim().toLowerCase()) || "into".equals(fromNext.trim().toLowerCase())) {
						break;
					}
					// 如果是,则前面的就是表名
					if (",".equals(fromNext.trim()) == false
							&& ("from".equals(words[i.num - 1]) || ",".equals(words[i.num - 1]))
							|| "join".equals(words[i.num - 1])) {
						tables.add(words[i.num]);
					} // else // 什么也不做
				}
			}

			if (i.num >= words.length)
				break;

			// 如果遇到update
			if ("update".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				if (i.num >= words.length)
					break;
				tables.add(words[i.num]);

			}

			if (i.num >= words.length)
				break;

			// 如果遇到into
			if ("into".equals(words[i.num].trim().toLowerCase())) {
				i.num++;
				if (i.num >= words.length)
					break;
				tables.add(words[i.num]);
			}
		}
	}

	/**
	 * 分词，在做SQL分析之前，先进行词法分析，找出SQL里面有意义的词汇
	 * 
	 * @param sql
	 * @return
	 */
	public static String[] splitWord(String sql, LexicalAnalysisProcessor processor) {

		List<String> words = new ArrayList<String>();

		String[] spaceSplit = sql.split(" ");

		for (String word : spaceSplit) {
			word = word.replaceAll(",", "#,#");
			word = word.replaceAll("\\(", "#(#");
			word = word.replaceAll("\\)", "#)#");

			String[] wordSplits = word.split("#");
			for (String wordSplit : wordSplits) {
				if (StringHelper.isBlankOrNull(wordSplit)) {
					continue;
				}
				words.add(wordSplit);
				if (processor != null) {
					processor.process(wordSplit);
				}
			}
		}

		String[] result = new String[words.size()];
		words.toArray(result);
		return result;
	}
}

/**
 * sql词法分析处理器，以便在一次词法扫描过程，嵌入额外的动作
 */
abstract class LexicalAnalysisProcessor {
	/**
	 * 
	 * @param word
	 *            词汇
	 */
	public abstract void process(String word);

}
