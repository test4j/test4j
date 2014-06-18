package org.test4j.spec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.spring.utility.SpringModuleHelper;
import org.test4j.spec.annotations.Mix;
import org.test4j.spec.annotations.StoryFile;
import org.test4j.spec.exceptions.ScenarioAssertError;
import org.test4j.spec.exceptions.SkipStepException;
import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.IScenarioStep;
import org.test4j.spec.inner.ISpecMethod;
import org.test4j.spec.inner.ISpecMethod.SpecMethodID;
import org.test4j.spec.inner.ISpecPrinter;
import org.test4j.spec.inner.StepType;
import org.test4j.spec.printer.MoreSpecPrinter;
import org.test4j.spec.scenario.ExceptionJSpecScenario;
import org.test4j.spec.scenario.Story;
import org.test4j.spec.scenario.TestScenario;
import org.test4j.spec.scenario.step.SpecMethod;
import org.test4j.spec.storypath.StoryPath;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.datagen.DataProviderIterator;

/**
 * @author darui.wudr 2013-1-10 下午8:33:04
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JSpecExecutorFactory implements ISpecExecutorFactory {

    @Override
    public Map<SpecMethodID, ISpecMethod> findMethodsInSpec(Class<? extends ISpec> clazz) {
        Map<SpecMethodID, ISpecMethod> allMethods = SpecMethod.findMethods(clazz);
        Mix mix = AnnotationHelper.getClassLevelAnnotation(Mix.class, clazz);
        if (mix == null) {
            return allMethods;
        }
        for (Class claz : mix.value()) {
            Map<SpecMethodID, ISpecMethod> stepMethods = SpecMethod.findMethods(claz);
            addMethods(allMethods, stepMethods);
        }
        return allMethods;
    }

    @Override
    public Map<String, Steps> newSteps(ISpec spec) {
        Map<String, Steps> stepsInstances = new HashMap<String, Steps>();
        Mix mix = AnnotationHelper.getClassLevelAnnotation(Mix.class, spec.getClass());
        if (mix == null) {
            return stepsInstances;
        }
        for (Class<? extends Steps> claz : mix.value()) {
            try {
                Steps instance = claz.newInstance();
                SpringModuleHelper.setSpringBean(instance);
                instance.setSharedData(spec.getSharedData());
                stepsInstances.put(claz.getName(), instance);
            } catch (Exception e) {
                String error = "new " + claz.getName() + " error, the Steps must have default Constructor. error:"
                        + e.getMessage();
                throw new RuntimeException(error, e);
            }
        }
        return stepsInstances;
    }

    private void addMethods(Map<SpecMethodID, ISpecMethod> allMethods, Map<SpecMethodID, ISpecMethod> stepMethods) {
        for (Map.Entry<SpecMethodID, ISpecMethod> entry : stepMethods.entrySet()) {
            if (allMethods.containsKey(entry.getKey())) {
                ISpecMethod existed = allMethods.get(entry.getKey());
                ISpecMethod method = entry.getValue();
                String info = String.format("duplicated muthod[%s] declared by class[%s] and class[%s].",
                        existed.getMethodName(), existed.getClazzName(), method.getClazzName());
                throw new RuntimeException(info);
            }
            allMethods.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public ISpecPrinter newSpecPrinter() {
        return new MoreSpecPrinter();
    }

    @Override
    public DataProviderIterator<IScenario> findScenario(Class<? extends ISpec> clazz) {
        try {
            Set<Integer> indexs = findScenarioRunIndex();

            StoryFile storyFile = AnnotationHelper.getClassLevelAnnotation(StoryFile.class, clazz);
            StoryPath storyPath = StoryPath.factory(clazz, storyFile);
            String encoding = this.getEncoding(storyFile);
            Story story = storyPath.getStory(storyFile, encoding);
            if (story == null || story.getScenarios() == null || story.getScenarios().size() == 0) {
                Exception e = new RuntimeException("no scenario defined in spec " + clazz.getName());
                return ExceptionJSpecScenario.iterator(e);
            }
            DataProviderIterator<IScenario> it = new DataProviderIterator<IScenario>();
            int index = 1;
            for (IScenario scenario : story.getScenarios()) {
                if (indexs == null || indexs.contains(index)) {
                    TestScenario testScenario = new TestScenario(story.getBeforeScenario(), scenario,
                            story.getAfterScenario());
                    it.data(testScenario);
                }
                index++;
            }
            return it;
        } catch (Throwable e) {
            return ExceptionJSpecScenario.iterator(e);
        }
    }

    public String getEncoding(StoryFile storyFile) {
        String default_encoding = ConfigHelper.getString("jspec.file.encoding", "utf8");
        if (storyFile == null) {
            return default_encoding;
        } else {
            String encoding = storyFile.encoding();
            return StringHelper.isBlankOrNull(encoding) ? default_encoding : encoding;
        }
    }

    /**
     * 需要被执行的场景序号,序号从1开始
     */
    public static final String SCENARIO_RUN_INDEX1 = "SIndex";

    public static final String SCENARIO_RUN_INDEX2 = "si";

    public static Set<Integer> findScenarioRunIndex() {
        String sIndexes = System.getProperty(SCENARIO_RUN_INDEX1);
        if (StringHelper.isBlankOrNull(sIndexes)) {
            sIndexes = System.getProperty(SCENARIO_RUN_INDEX2);
        }
        if (StringHelper.isBlankOrNull(sIndexes)) {
            return null;
        }
        if ("0".equals(sIndexes)) {
            return null;
        }
        String[] items = sIndexes.split(",");
        Set<Integer> set = new HashSet<Integer>();
        for (String item : items) {
            try {
                Integer no = Integer.parseInt(item);
                set.add(no);
            } catch (Exception e) {
                // do nothing
            }
        }
        return set;
    }

    @Override
    public void runScenario(ISpec spec, IScenario scenario, Map<SpecMethodID, ISpecMethod> methods, ISpecPrinter printer)
            throws Throwable {
        scenario.validate();

        List<IScenarioStep> steps = scenario.getSteps();
        List<Throwable> assertErrors = new ArrayList<Throwable>();
        Throwable suspend = null;
        for (IScenarioStep step : steps) {
            if (step.isSuspend() || suspend != null) {
                step.setError(SkipStepException.instance);
                continue;
            }
            Throwable error = this.runSingleStep(spec, step, methods);
            if (error != null) {
                assertErrors.add(error);
                step.setError(error);
            }
            if (step.getType() != StepType.Then) {
                suspend = error;
            }
        }
        printer.printScenario(spec, scenario);
        if (suspend != null) {
            throw suspend;
        } else if (assertErrors.size() != 0) {
            throw new ScenarioAssertError(scenario, assertErrors);
        }
    }

    /**
     * 执行测试场景的单个方法
     * 
     * @param spec
     * @param step
     * @param methods
     * @return
     */
    private Throwable runSingleStep(ISpec spec, IScenarioStep step, Map<SpecMethodID, ISpecMethod> methods) {
        SpecMethodID id = step.getSpecMethodID();
        if (methods.containsKey(id) == false) {
            throw new RuntimeException(String.format("can't find %s in class[%s]", id, spec.getClass().getName()));
        }
        ISpecMethod specMethod = methods.get(id);
        try {
            specMethod.execute(spec, step);
            return null;
        } catch (Throwable e) {
            String error = String.format("Invoke class (%s.java:1) method[%s] error:\n%s!", specMethod.getClazzName(),
                    specMethod.getMethodName(), e.getMessage());
            MessageHelper.error(error, e);
            return new RuntimeException(error, e);
        }
    }
}
