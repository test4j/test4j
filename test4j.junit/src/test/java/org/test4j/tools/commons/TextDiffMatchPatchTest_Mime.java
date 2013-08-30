package org.test4j.tools.commons;

import java.util.LinkedList;

import org.jtester.junit.JTester;
import org.junit.Before;
import org.junit.Test;
import org.test4j.tools.commons.TextDiffMatchPatch;
import org.test4j.tools.commons.TextDiffMatchPatch.Diff;

public class TextDiffMatchPatchTest_Mime implements JTester {
    private TextDiffMatchPatch textdiff;

    @Before
    public void setUp() {
        // Create an instance of the TextDiffMatchPatch object.
        textdiff = new TextDiffMatchPatch();
    }

    @Test
    public void test2String() {
        String str1 = "I am string one\nAnother 中文";
        String str2 = "I am string two\nabcd Another";

        LinkedList<Diff> list = textdiff.diff_main(str1, str2);

        // String text = textdiff.diff_prettyHtml(list);
        String text = textdiff.diff_text2(list);
        System.out.println("===========================");
        System.out.println(text);
    }

}
