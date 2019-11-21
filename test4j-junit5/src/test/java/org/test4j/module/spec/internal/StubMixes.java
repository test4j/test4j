package org.test4j.module.spec.internal;

import org.test4j.module.spec.annotations.Mix;
import org.test4j.module.spec.annotations.Mixes;
import org.test4j.module.spec.internal.mix.StubInterface1Mix;
import org.test4j.module.spec.internal.mix.StubInterface2Mix;

/**
 * @author darui.wu
 * @create 2019/11/15
 */
@Mixes
public class StubMixes {

	@Mix
	public StubInterface1Mix stubInterface1Mix;

	@Mix
	public StubInterface2Mix stubInterface2Mix;
}