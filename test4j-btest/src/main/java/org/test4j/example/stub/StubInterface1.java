package org.test4j.example.stub;

import org.test4j.example.spring.ServiceA;
import org.test4j.exception.Test4JException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author:darui.wu Created by darui.wu on 2019/11/15.
 */
public interface StubInterface1 {
    void aVoidMethod(int intArg, Double doubleArg) throws Test4JException;

    int aFunctionMethod(Function<List<Map<String, ?>>, String> function, List<? extends Set<ServiceA>> list);
}