package org.test4j.datafilling.filler.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.test4j.datafilling.Filler;
import org.test4j.datafilling.common.AttributeInfo;
import org.test4j.datafilling.filler.PoJoFiller;
import org.test4j.datafilling.strategy.DataFactory;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.Reflector;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConstructorFiller extends PoJoFiller {

    public ConstructorFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
        super(strategy, argsTypeMap);
    }

    /**
     * There are public constructors. We need the no-arg one for now.
     * 
     * @param constructors
     * @param genericTypeArgs
     * @return
     */
    public Object fillingWithPublicConstructor(Class clazz, Constructor[] constructors) {
        for (Constructor constructor : constructors) {
            try {
                Object[] constructorArgs = getParametersForConstructor(clazz, constructor);
                Object retValue = constructor.newInstance(constructorArgs);
                return retValue;
            } catch (Exception e) {
                String error = "Couldn't create attribute with constructor: " + constructor
                        + ". Will check if other constructors are available";
                MessageHelper.error(error, e);
            }
        }
        return null;
    }

    /**
     * Given a constructor it manufactures and returns the parameter values
     * required to invoke it
     * 
     * @param clazz The POJO class containing the constructor
     * @param constructor The constructor for which parameter values are
     *            required
     * @param typeArgsMap The generic type arguments for the current generic
     *            class instance
     * @return The parameter values required to invoke the constructor
     * @throws Exception
     */
    private Object[] getParametersForConstructor(Class clazz, Constructor constructor) throws Exception {
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterValues = new Object[parameterTypes.length];

        int index = 0;
        for (Class parameterType : parameterTypes) {
            if (parameterType.equals(clazz)) {
                parameterValues[index] = Reflector.instance.newInstance(clazz);
            } else {
                List<Annotation> annotations = Arrays.asList(parameterAnnotations[index]);
                Type paraGenType = constructor.getGenericParameterTypes()[index];
                AttributeInfo paraAttribute = AttributeInfo.exactArgAttributeInfo(paraGenType, argsTypeMap);
                paraAttribute.setAttrAnnotations(annotations);
                Object para = new Filler(this.strategy, argsTypeMap).fillingAttribute(paraAttribute);
                parameterValues[index] = para;
            }
            index++;
        }
        return parameterValues;
    }
}
