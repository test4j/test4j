package org.jtester.module.spring.strategy;

import org.jtester.module.core.utility.IPropConst;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.tools.commons.AnnotationHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

@SuppressWarnings("rawtypes")
public class LazySpringContext extends JTesterSpringContext {

	public LazySpringContext(String[] configLocations, boolean refresh) throws BeansException {
		super(configLocations, refresh, true);
	}

	@Override
	protected Class getTestedClazzz() {
		Class testedClazz = super.getTestedClazzz();
		Class declareAnnotationClazz = AnnotationHelper.getClassWithAnnotation(SpringContext.class,
				testedClazz);
		if (declareAnnotationClazz == null) {
			throw new RuntimeException(
					"there must be some error, can't find @SpringApplicationContext annotation declared by class["
							+ testedClazz.getName() + "] or it's parent.");
		}
		return declareAnnotationClazz;
	}

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * 但在共享模式中取消动态加载模式，改为全部从文件中读取
	 */
	@Override
	protected void dynamicRegisterBean(DefaultListableBeanFactory beanFactory, Class testedClazz) {
	}

	/**
	 * 下面这段本来想将spring初始化时所有的bean都置成lazy-init的模式<br>
	 * 但实现中碰到问题,主要是tracer的aop初始化上出错。
	 */
	@Override
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
		if (IPropConst.SPRING_LAZY_LOAD) {
			beanDefinitionReader.setEventListener(new JTesterReaderEventListener());
		}
	}

	/**
	 * @Override protected void customizeBeanFactory(DefaultListableBeanFactory
	 *           beanFactory) { super.customizeBeanFactory(beanFactory);
	 *           beanFactory.setAllowEagerClassLoading(false); }
	 */

	/**
	 * 自定义spring ReaderEventListener<br>
	 * 参见{@link DefaultBeanDefinitionDocumentReader} 和
	 * {@link BeanDefinitionParserDelegate}的initDefaults方法
	 * 
	 * <pre>
	 * 	  复写defaultsRegistered方法，在跑单元测试中，强制设置default-lazy-init=true属性
	 * </pre>
	 */
	public static class JTesterReaderEventListener extends EmptyReaderEventListener {
		@Override
		public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
			if (defaultsDefinition instanceof DocumentDefaultsDefinition) {
				DocumentDefaultsDefinition docDefault = (DocumentDefaultsDefinition) defaultsDefinition;
				docDefault.setLazyInit("true");
			}
		}
	}
}
