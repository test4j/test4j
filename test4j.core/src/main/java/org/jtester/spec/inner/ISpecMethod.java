package org.jtester.spec.inner;

import org.jtester.spec.ISpec;

/**
 * 场景测试类中的方法描述
 * 
 * @author darui.wudr 2013-1-10 下午7:12:10
 */
public interface ISpecMethod {

    /**
     * 执行场景步骤
     * 
     * @param jSpec
     * @param step
     * @return
     */
    Object execute(ISpec spec, IScenarioStep step);

    /**
     * 返回定义方法的类全路径名称
     * 
     * @return
     */
    String getClazzName();

    /**
     * 返回定义方法的名称
     * 
     * @return
     */
    String getMethodName();

    /**
     * 场景测试类中测试方法ID
     * 
     * @author darui.wudr 2013-1-10 下午7:25:48
     */
    public class SpecMethodID {
        /**
         * step方法名称
         */
        private final String methodName;
        /**
         * 方法参数个数
         */
        private final int    argCount;

        public SpecMethodID(String methodName, int argCount) {
            this.methodName = methodName;
            this.argCount = argCount;
        }

        public String getMethodName() {
            return methodName;
        }

        @Override
        public String toString() {
            return "SpecMethod[method=" + methodName + ", argCount=" + argCount + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + argCount;
            result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SpecMethodID other = (SpecMethodID) obj;
            if (argCount != other.argCount)
                return false;
            if (methodName == null) {
                if (other.methodName != null)
                    return false;
            } else if (!methodName.equals(other.methodName))
                return false;
            return true;
        }
    }
}
