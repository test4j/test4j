package org.jtester.datafilling.filler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.datafilling.Filler;
import org.jtester.datafilling.common.AttributeInfo;
import org.jtester.datafilling.common.ClassFieldInfo;
import org.jtester.datafilling.exceptions.PoJoFillException;
import org.jtester.datafilling.filler.pojo.ConstructorFiller;
import org.jtester.datafilling.filler.pojo.FactoryMethodFiller;
import org.jtester.datafilling.filler.pojo.SetterMethodFiller;
import org.jtester.datafilling.strategy.DataFactory;

/**
 * PoJo对象填充器
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PoJoFiller extends Filler {

	public PoJoFiller(DataFactory strategy, Map<String, Type> argsTypeMap) {
		super(strategy, argsTypeMap);
	}

	/**
	 * Generic method which returns an instance of the given class filled with
	 * dummy values
	 * 
	 * @param pojoAttr
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @param depth
	 *            How many times {@code dtoClass} has been found
	 * @return An instance of <T> filled with dummy values
	 * 
	 * @throws PoJoFillException
	 *             if a problem occurred while creating a POJO instance or while
	 *             setting its state
	 */
	public <T> T fillingPoJo(AttributeInfo pojoAttr, int depth) throws PoJoFillException {
		try {
			ClassFieldInfo classInfo = pojoAttr.getClassInfo();
			final Map<String, Type> typeArgsMap = pojoAttr.getArgsTypeMap();
			if (classInfo.getClassSetters().isEmpty() || pojoAttr.isJavax()) {
				Object o = fillingWithConstructorsOrFactory(pojoAttr);
				return (T) o;
			} else {
				SetterMethodFiller filler = new SetterMethodFiller(this.strategy, typeArgsMap);
				Object o = filler.fillingWithSetter(pojoAttr, depth);
				return (T) o;
			}
		} catch (Exception e) {
			throw new PoJoFillException("An filling exception occurred:" + e.getMessage(), e);
		}
	}

	/**
	 * It attempts to create an instance of the given class<br>
	 * 使用工厂方法创建实例对象
	 * <p>
	 * This method attempts to create an instance of the given argument for
	 * classes without setters. These may be either immutable classes (e.g. with
	 * final attributes and no setters) or Java classes (e.g. belonging to the
	 * java / javax namespace).<br>
	 * In case the class does not provide a public, no-arg constructor (e.g.
	 * Calendar), this method attempts to find a , no-args, factory method (e.g.
	 * getInstance()) and it invokes it
	 * </p>
	 * 
	 * @param clazz
	 *            The class for which a new instance is required
	 * 
	 * @return An instance of the given class
	 * @throws Exception
	 */
	public Object fillingWithConstructorsOrFactory(AttributeInfo attrinbuteInfo) throws Exception {
		Constructor[] constructors = attrinbuteInfo.getPublicConstructor();
		if (constructors.length == 0) {
			FactoryMethodFiller filler = new FactoryMethodFiller(strategy, null);
			Object o = filler.newInstanceUseFactoryMethod(attrinbuteInfo);
			return o;
		} else {
			ConstructorFiller filler = new ConstructorFiller(strategy, null);
			Object o = filler.fillingWithPublicConstructor(attrinbuteInfo.getAttrClaz(), constructors);
			return o;
		}
	}
}
