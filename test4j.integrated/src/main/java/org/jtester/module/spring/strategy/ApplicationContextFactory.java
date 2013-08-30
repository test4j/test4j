/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtester.module.spring.strategy;

import java.util.List;

/**
 * Factory for creating Spring <code>ApplicationContext</code>s.
 * 
 */
public interface ApplicationContextFactory {

	/**
	 * Create an <code>ApplicationContext</code>, in which the complete list of
	 * the given resources is loaded. The way in which these locations are
	 * interpreted depends on the concrete implementation of the interface. More
	 * in particular, the returned instance is an instance of
	 * <code>ConfigurableApplicationContext</code>, that is not yet
	 * <i>refreshed</i>, i.e. the method
	 * <code>ConfigurableApplicationContext.refresh()</code> has neither been
	 * called explicitly, nor implicitly by invoking a constructor that also
	 * makes sure the configuration is processed by calling the
	 * <code>refresh</code> method.
	 * 
	 * @param locations
	 *            The configuration file locations, not null
	 * @return A <code>ConfigurableApplicationContext</code>, on which the
	 *         <code>refresh()</code> method hasn't been called yet
	 */
	JTesterSpringContext createApplicationContext(List<String> locations, boolean refresh, boolean shared);
}
