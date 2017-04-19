/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.util.LegacyUtils;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.SQLDataLoader;
import org.kuali.rice.test.TestUtilities;
import org.kuali.rice.test.lifecycles.KEWXmlDataLoaderLifecycle;
import org.kuali.rice.test.runners.BootstrapTest;
import org.kuali.rice.test.runners.LoadTimeWeavableTestRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.namespace.QName;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Default test base for a full KRAD enabled integration test
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK_CLEAR_DB)
@RunWith(LoadTimeWeavableTestRunner.class)
@BootstrapTest(KRADTestCase.BootstrapTest.class)
public abstract class KRADTestCase extends BaselineTestCase {
    private static final String SQL_FILE = "classpath:org/kuali/rice/krad/test/DefaultSuiteTestData.sql";
    private static final String XML_FILE = "classpath:org/kuali/rice/krad/test/DefaultSuiteTestData.xml";
    private static final String KRAD_MODULE_NAME = "krad";

    protected DataDictionary dd;

    protected static SpringResourceLoader kradTestHarnessSpringResourceLoader;

    private boolean legacyContext = false;

    public KRADTestCase() {
        super(KRAD_MODULE_NAME);
    }

    /**
     * propagate constructor
     * @param moduleName - the name of the module
     */
    public KRADTestCase(String moduleName) {
        super(moduleName);
    }

    protected ConfigurableApplicationContext getKRADTestHarnessContext() {
        return kradTestHarnessSpringResourceLoader.getContext();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setUpLegacyContext();

    }

    @Override
    public void tearDown() throws Exception {
        try {
            tearDownLegacyContext();
        } finally {
            super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    protected void setUpLegacyContext() {
        if (getTestMethod().getAnnotation(Legacy.class) != null || getClass().getAnnotation(Legacy.class) != null) {
            LegacyUtils.beginLegacyContext();
            legacyContext = true;
        }
    }

    protected void tearDownLegacyContext() {
        if (getTestMethod().getAnnotation(Legacy.class) != null || getClass().getAnnotation(Legacy.class) != null) {
            if (legacyContext) {
                LegacyUtils.endLegacyContext();
                legacyContext = false;
            }
        }
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();

        List<Class> classes = TestUtilities.getHierarchyClassesToHandle(getClass(),
                new Class[]{TestDictionaryConfig.class}, new HashSet<String>());

        // if annotation is present then initialize test data dictionary (setup once per suite)
        if (!classes.isEmpty()) {
            ConfigurableApplicationContext context  = new ClassPathXmlApplicationContext("TestDataDictionary.xml");
            dd = (DataDictionary) context.getBean("testDataDictionary");

            // add any additional dictionary files required by the test
            for (Class c : classes) {
                if (c.isAnnotationPresent(TestDictionaryConfig.class)) {
                    TestDictionaryConfig testDictionaryConfig = (TestDictionaryConfig) c.getAnnotation(
                            TestDictionaryConfig.class);

                    String namespaceCode = testDictionaryConfig.namespaceCode();
                    String dictionaryFileString = testDictionaryConfig.dataDictionaryFiles();

                    String[] dictionaryFiles = StringUtils.split(dictionaryFileString, ",");
                    for (String dictionaryFile : dictionaryFiles) {
                        LOG.info("Adding test data dictionary file: " + dictionaryFile);

                        dd.addConfigFileLocation(namespaceCode, dictionaryFile);
                    }
                }
            }

            dd.parseDataDictionaryConfigurationFiles(false);
            dd.validateDD(false); // Validation performs some necessary post-processing of the beans - we need to run this each time we add new files
            dd.performBeanOverrides();
        }
    }

    /**
     * Returns an instance of the bean with the given id that has been configured in the test dictionary
     *
     * @param id - id of the bean definition
     * @return Object instance of the given bean class, or null if not found or dictionary is not loaded
     */
    protected Object getTestDictionaryObject(String id) {
        if (dd != null) {
            return dd.getDictionaryBean(id);
        }

        return null;
    }

    @Override
    protected List<Lifecycle> getSuiteLifecycles() {
        List<Lifecycle> suiteLifecycles = super.getSuiteLifecycles();
        suiteLifecycles.add(new KEWXmlDataLoaderLifecycle(XML_FILE));

        return suiteLifecycles;
    }

    @Override
    protected List<String> getPerTestTablesNotToClear() {
        List<String> tablesNotToClear = new ArrayList<String>();
        tablesNotToClear.add("KRIM_.*");
        tablesNotToClear.add("KRCR_.*");
        tablesNotToClear.add("KREW_.*");
        return tablesNotToClear;
    }

    @Override
    protected void loadSuiteTestData() throws Exception {
        super.loadSuiteTestData();
        new SQLDataLoader(SQL_FILE, ";").runSql();
    }

    @Override
    protected Lifecycle getLoadApplicationLifecycle() {
        // cache the KRAD test harness spring resource loader
        // this is not great because it doesn't conform to the lifecycle
        // ...but why are we creating sub-resourceloaders instead of just adding locations to the test harness context?
        if (kradTestHarnessSpringResourceLoader == null) {
            kradTestHarnessSpringResourceLoader = new SpringResourceLoader(new QName("KRADTestResourceLoader"),
                    "classpath:KRADTestHarnessSpringBeans.xml", null);
            kradTestHarnessSpringResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
        }
        return kradTestHarnessSpringResourceLoader;
    }

    /**
     * Annotation which indicates that a Legacy Context should be used for this individual test method or for all tests
     * in an annotated class.
     *
     * @see org.kuali.rice.krad.util.LegacyUtils#doInLegacyContext(java.util.concurrent.Callable)
     */
    @Target({TYPE, METHOD})
    @Retention(RUNTIME)
    public @interface Legacy {}

    public static final class BootstrapTest extends KRADTestCase {
        @Test
        public void bootstrapTest() {};
    }

}
