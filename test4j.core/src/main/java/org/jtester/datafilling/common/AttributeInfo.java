package org.jtester.datafilling.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.tools.commons.PrimitiveHelper;
import org.jtester.tools.commons.Reflector;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.reflector.FieldAccessor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AttributeInfo {
	/**
	 * 属性所在类
	 */
	private Class pojoClaz;
	/**
	 * 属性名称
	 */
	private String attrName;
	/**
	 * 属性的java类型，比如Map，Collection，List等
	 */
	private Class attrClaz;
	/**
	 * java类型的范型参数<br>
	 * 如Map<String,Integer>就是 new Type[]{String.class,Integer}
	 */
	private Type[] attrArgs;

	/** The attribute annotations */
	private List<Annotation> attrAnnotations;

	public AttributeInfo() {
		this(Object.class);
	}

	public AttributeInfo(Class attrClaz) {
		this(null, null, attrClaz);
	}

	public AttributeInfo(Class pojoClaz, String attrName, Class attrClaz) {
		this.pojoClaz = pojoClaz;
		this.attrName = attrName;
		this.attrClaz = attrClaz;
		this.attrArgs = new Type[0];
		this.attrAnnotations = new ArrayList<Annotation>();
	}

	public Class getAttrClaz() {
		return attrClaz;
	}

	public AttributeInfo setAttrClaz(Class clazz) {
		this.attrClaz = clazz;
		return this;
	}

	public Type[] getAttrArgs() {
		return attrArgs;
	}

	public AttributeInfo setAttrArgs(Type... arguments) {
		this.attrArgs = arguments;
		return this;
	}

	public String getAttrName() {
		return attrName;
	}

	public AttributeInfo setAttrName(String attrName) {
		this.attrName = attrName;
		return this;
	}

	public Class getPojoClaz() {
		return pojoClaz;
	}

	public AttributeInfo setPojoClaz(Class pojoClaz) {
		this.pojoClaz = pojoClaz;
		return this;
	}

	public List<Annotation> getAttrAnnotations() {
		if (attrAnnotations == null) {
			return new ArrayList<Annotation>();
		} else {
			return attrAnnotations;
		}
	}

	public AttributeInfo setAttrAnnotations(List<Annotation> attrAnnotations) {
		this.attrAnnotations = attrAnnotations;
		return this;
	}

	public boolean isNestedAttribute() {
		return this.attrClaz.equals(this.pojoClaz);
	}

	public ClassFieldInfo getClassInfo() {
		ClassFieldInfo classInfo = ClassFieldInfo.getClassInfo(this.attrClaz);
		return classInfo;
	}

	/**
	 * 返回属性字段定义
	 * 
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public Field getDeclaredField() throws SecurityException, NoSuchFieldException {
		if (pojoClaz == null || StringHelper.isBlankOrNull(attrName)) {
			throw new NoSuchFieldException("the pojo or the attribute name is null!");
		} else {
			return pojoClaz.getDeclaredField(attrName);
		}
	}

	/**
	 * 返回属性类的public构造函数
	 * 
	 * @return
	 */
	public Constructor[] getPublicConstructor() {
		return this.attrClaz.getConstructors();
	}

	/**
	 * 返回属性类的可能工厂方法
	 * 
	 * @return
	 */
	public List<Method> getFactoryMethods() {
		Method[] methods = this.attrClaz.getDeclaredMethods();

		List<Method> factoryMethods = new ArrayList<Method>();
		for (Method method : methods) {
			if (!Modifier.isStatic(method.getModifiers()) || !method.getReturnType().equals(attrClaz)) {
				continue;
			}
			factoryMethods.add(method);
		}
		return factoryMethods;
	}

	/**
	 * 根据属性（getter）返回属性值
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <T> T getAttributeValue() throws InstantiationException, IllegalAccessException {
		if (pojoClaz == null || StringHelper.isBlankOrNull(attrName)) {
			return null;
		} else {
			Object instance = Reflector.instance.newInstance(this.pojoClaz);
			FieldAccessor fieldAccessor = new FieldAccessor(this.pojoClaz, this.attrName);
			Object o = fieldAccessor.get(instance);
			return (T) o;
		}
	}

	public <T> T newInstance() {
		Object o = Reflector.instance.newInstance(this.attrClaz);
		return (T) o;
	}

	public Class getComponentType() {
		return this.attrClaz.getComponentType();
	}

	public String getPoJoName() {
		return this.pojoClaz == null ? "<null>" : this.pojoClaz.getName();
	}

	public boolean isCollection() {
		return Collection.class.isAssignableFrom(attrClaz);
	}

	public boolean isPrimitive() {
		return PrimitiveHelper.isPrimitiveTypeOrRelative(attrClaz);
	}

	public boolean isMap() {
		return Map.class.isAssignableFrom(attrClaz);
	}

	public boolean isString() {
		return attrClaz.equals(String.class);
	}

	public boolean isArray() {
		return attrClaz.isArray();
	}

	public boolean isEnum() {
		return attrClaz.isEnum();
	}

	public boolean isJavax() {
		return attrClaz.getName().startsWith("java.") || attrClaz.getName().startsWith("javax.");
	}

	public boolean isObject() {
		return Object.class.equals(attrClaz);
	}

	public boolean isEmptyArgs() {
		return attrArgs == null || attrArgs.length == 0;
	}

	public Object[] getEnumConstants() {
		return this.attrClaz.getEnumConstants();
	}

	/**
	 * 返回第n个泛型参数的具体属性类型
	 * 
	 * @param index
	 * @param typeArgsMap
	 * @return
	 */
	public AttributeInfo getArgTypeAttribute(int index, Map<String, Type> typeArgsMap) {
		if (isEmptyArgs()) {
			return new AttributeInfo(Object.class);
		}
		if (index >= this.attrArgs.length || index < 0) {
			throw new IllegalStateException("Illegal generic type are expected, the index[" + index + "] out range of "
					+ this.attrArgs.length);
		}
		AttributeInfo ainfo = AttributeInfo.exactArgAttributeInfo(this.attrArgs[index], typeArgsMap);
		return ainfo;
	}

	/**
	 * 根据泛型参数类型和泛型名称类型对应表析取泛型具体类型及其泛型参数列表
	 * 
	 * @param argType
	 * @param typeArgsMap
	 * @return
	 */
	public static AttributeInfo exactArgAttributeInfo(Type argType, Map<String, Type> typeArgsMap) {
		AttributeInfo genericClazz = new AttributeInfo(Object.class);
		if (argType instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) argType;
			genericClazz.setAttrClaz((Class) pType.getRawType());
			genericClazz.setAttrArgs(pType.getActualTypeArguments());
		} else if (argType instanceof TypeVariable) {
			final String typeName = ((TypeVariable) argType).getName();
			final Type type = typeArgsMap.get(typeName);
			return exactArgAttributeInfo(type, typeArgsMap);
		} else if (argType instanceof GenericArrayType) {
			Type type = ((GenericArrayType) argType).getGenericComponentType();
			genericClazz = exactArgAttributeInfo(type, typeArgsMap);
			genericClazz.setAttrClaz(Array.newInstance(genericClazz.getAttrClaz(), 0).getClass());
		} else {
			genericClazz.setAttrClaz((Class) argType);
		}
		return genericClazz;
	}

	private Map<String, Type> argsTypeMap = null;

	public AttributeInfo setArgsTypeMap(Map<String, Type> argsTypeMap) {
		this.argsTypeMap = argsTypeMap;
		return this;
	}

	/**
	 * 解析泛型的名称和类型对<br>
	 * eg: T -> String, E -> Enum
	 * 
	 * @param genericTypeArgs
	 * @return
	 */
	public Map<String, Type> getArgsTypeMap() {
		if (argsTypeMap != null) {
			return argsTypeMap;
		}
		final TypeVariable[] typeParameters = this.attrClaz.getTypeParameters();
		if (typeParameters.length > this.attrArgs.length) {
			String info = String.format("Missing generic type arguments, expected %d but found %d, so returning null.",
					typeParameters.length, attrArgs.length);
			throw new RuntimeException(info);
		}

		this.argsTypeMap = new HashMap<String, Type>();
		for (int i = 0; i < typeParameters.length; i++) {
			argsTypeMap.put(typeParameters[i].getName(), attrArgs[i]);
		}
		return argsTypeMap;
	}

	public boolean isInterface() {
		return this.attrClaz.isInterface();
	}

	public boolean isAbstract() {
		return Modifier.isAbstract(this.attrClaz.getModifiers());
	}
}
