/**
 * Copyright 2005-2019 The Kuali Foundation
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
package org.kuali.rice.coreservice.impl.parameter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.kuali.rice.core.api.config.property.ConfigStrLookup;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Default implementation of {@link ParameterService}.
 */
public class ParameterServiceImpl implements ParameterService, InitializingBean {

    private static final long PARAMETER_WATCH_FREQUENCY = 5000;

    private static final StrSubstitutor CONFIG_SUBSTITUTOR = new StrSubstitutor(new ConfigStrLookup());

    private KualiModuleService kualiModuleService;
    private ParameterRepositoryService parameterRepositoryService;
    private TaskScheduler taskScheduler;

    private String applicationId = KRADConstants.DEFAULT_PARAMETER_APPLICATION_ID;

    private Multimap<ParameterKey, Consumer<Parameter>> watches;
    private Map<ParameterKey, String> watchedValues;

    private Object watchLock = new Object();

    @Override
    public void afterPropertiesSet() throws Exception {
        watches = ArrayListMultimap.create();
        watchedValues = new HashMap<>();
        if (taskScheduler != null) {
            taskScheduler.scheduleAtFixedRate(this::checkWatches, PARAMETER_WATCH_FREQUENCY);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter createParameter(Parameter parameter) {
        return parameterRepositoryService.createParameter(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter updateParameter(Parameter parameter) {
        return parameterRepositoryService.updateParameter(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter getParameter(String namespaceCode, String componentCode, String parameterName) {
        return exec(new Fun<Parameter>() {
            @Override public Parameter f(ParameterKey key) {
                return parameterRepositoryService.getParameter(key);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter getParameter(Class<?> componentClass, String parameterName) {
        return exec(new Fun<Parameter>() {
            @Override public Parameter f(ParameterKey key) {
                return parameterRepositoryService.getParameter(key);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean parameterExists(String namespaceCode, String componentCode, String parameterName) {
        return exec(new Fun<Boolean>() {
            @Override
            public Boolean f(ParameterKey key) {
                return Boolean.valueOf(parameterRepositoryService.getParameter(key) != null);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean parameterExists(Class<?> componentClass, String parameterName) {
        return exec(new Fun<Boolean>() {
            @Override
            public Boolean f(ParameterKey key) {
                return Boolean.valueOf(parameterRepositoryService.getParameter(key) != null);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName) {
        return exec(new Fun<Boolean>() {
            @Override
            public Boolean f(ParameterKey key) {
                return parameterRepositoryService.getParameterValueAsBoolean(key);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName, Boolean defaultValue) {
        final Boolean value = getParameterValueAsBoolean(namespaceCode, componentCode, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getParameterValueAsBoolean(Class<?> componentClass, String parameterName) {
        return exec(new Fun<Boolean>() {
            @Override
            public Boolean f(ParameterKey key) {
                return parameterRepositoryService.getParameterValueAsBoolean(key);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getParameterValueAsBoolean(Class<?> componentClass, String parameterName, Boolean defaultValue) {
        final Boolean value = getParameterValueAsBoolean(componentClass, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName) {
        return exec(new Fun<String>() {
            @Override
            public String f(ParameterKey key) {
                return parameterRepositoryService.getParameterValueAsString(key);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName, String defaultValue) {
        final String value = getParameterValueAsString(namespaceCode, componentCode, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsString(Class<?> componentClass, String parameterName) {
        return exec(new Fun<String>() {
            @Override public String f(ParameterKey key) {
                return parameterRepositoryService.getParameterValueAsString(key);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsString(Class<?> componentClass, String parameterName, String defaultValue) {
        final String value = getParameterValueAsString(componentClass, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsFilteredString(String namespaceCode, String componentCode, String parameterName) {
        final String value = getParameterValueAsString(namespaceCode, componentCode, parameterName);
        return CONFIG_SUBSTITUTOR.replace(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsFilteredString(String namespaceCode, String componentCode, String parameterName, String defaultValue) {
        final String value = getParameterValueAsFilteredString(namespaceCode, componentCode, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsFilteredString(Class<?> componentClass, String parameterName) {
        final String value = getParameterValueAsString(componentClass, parameterName);
        return CONFIG_SUBSTITUTOR.replace(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValueAsFilteredString(Class<?> componentClass, String parameterName, String defaultValue) {
        final String value = getParameterValueAsString(componentClass, parameterName);
        return (value != null) ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getParameterValuesAsString(String namespaceCode, String componentCode, String parameterName) {
        return exec(new Fun<Collection<String>>() {
            @Override public Collection<String> f(ParameterKey key) {
                return parameterRepositoryService.getParameterValuesAsString(key);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getParameterValuesAsString(Class<?> componentClass, String parameterName) {
        return exec(new Fun<Collection<String>>() {
            @Override public Collection<String> f(ParameterKey key) {
                return parameterRepositoryService.getParameterValuesAsString(key);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getParameterValuesAsFilteredString(String namespaceCode, String componentCode, String parameterName) {
        Collection<String> unfilteredValues = getParameterValuesAsString(namespaceCode, componentCode, parameterName);
        Collection<String> filteredValues = new ArrayList<String>();

        for (String unfilteredValue : unfilteredValues) {
            filteredValues.add(CONFIG_SUBSTITUTOR.replace(unfilteredValue));
        }

        return filteredValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getParameterValuesAsFilteredString(Class<?> componentClass, String parameterName) {
        Collection<String> unfilteredValues = getParameterValuesAsString(componentClass, parameterName);
        Collection<String> filteredValues = new ArrayList<String>();

        for (String unfilteredValue : unfilteredValues) {
            filteredValues.add(CONFIG_SUBSTITUTOR.replace(unfilteredValue));
        }

        return filteredValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubParameterValueAsString(String namespaceCode, String componentCode, String parameterName, final String subParameterName) {
        return exec(new Fun<String>() {
            @Override public String f(ParameterKey key) {
                return parameterRepositoryService.getSubParameterValueAsString(key, subParameterName);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubParameterValueAsString(Class<?> componentClass, String parameterName, final String subParameterName) {
        return exec(new Fun<String>() {
            @Override public String f(ParameterKey key) {
                return parameterRepositoryService.getSubParameterValueAsString(key, subParameterName);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubParameterValueAsFilteredString(String namespaceCode, String componentCode, String parameterName, String subParameterName) {
        final String value = getSubParameterValueAsString(namespaceCode, componentCode, parameterName, subParameterName);
        return CONFIG_SUBSTITUTOR.replace(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubParameterValueAsFilteredString(Class<?> componentClass, String parameterName, String subParameterName) {
        final String value = getSubParameterValueAsString(componentClass, parameterName, subParameterName);
        return CONFIG_SUBSTITUTOR.replace(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSubParameterValuesAsString(String namespaceCode, String componentCode, String parameterName, final String subParameterName) {
        return exec(new Fun<Collection<String>>() {
            @Override public Collection<String> f(ParameterKey key) {
                return parameterRepositoryService.getSubParameterValuesAsString(key, subParameterName);
            }
        }, namespaceCode, componentCode, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSubParameterValuesAsString(Class<?> componentClass, String parameterName, final String subParameterName) {
        return exec(new Fun<Collection<String>>() {
            @Override public Collection<String> f(ParameterKey key) {
                return parameterRepositoryService.getSubParameterValuesAsString(key, subParameterName);
            }
        }, componentClass, parameterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSubParameterValuesAsFilteredString(String namespaceCode, String componentCode, String parameterName, String subParameterName) {
        Collection<String> unfilteredValues = getSubParameterValuesAsFilteredString(namespaceCode, componentCode, parameterName, subParameterName);
        Collection<String> filteredValues = new ArrayList<String>();

        for (String unfilteredValue : unfilteredValues) {
            filteredValues.add(CONFIG_SUBSTITUTOR.replace(unfilteredValue));
        }

        return filteredValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSubParameterValuesAsFilteredString(Class<?> componentClass, String parameterName, String subParameterName) {
        Collection<String> unfilteredValues = getSubParameterValuesAsFilteredString(componentClass, parameterName, subParameterName);
        Collection<String> filteredValues = new ArrayList<String>();

        for (String unfilteredValue : unfilteredValues) {
            filteredValues.add(CONFIG_SUBSTITUTOR.replace(unfilteredValue));
        }

        return filteredValues;
    }

    @Override
    public void watchParameter(String namespaceCode, String componentCode, String parameterName, Consumer<Parameter> consumer) {
       if (taskScheduler == null) {
           throw new UnsupportedOperationException("No TaskScheduler available.");
       }
       ParameterKey parameterKey = ParameterKey.create(applicationId, namespaceCode, componentCode, parameterName);
       Parameter parameter = getParameter(namespaceCode, componentCode, parameterName);
       synchronized (watchLock) {
           watches.put(parameterKey, consumer);
           watchedValues.put(parameterKey, nullSafeValue(parameter));
       }
    }

    public void setKualiModuleService(KualiModuleService kualiModuleService) {
        this.kualiModuleService = kualiModuleService;
    }

    public void setParameterRepositoryService(ParameterRepositoryService parameterRepositoryService) {
        this.parameterRepositoryService = parameterRepositoryService;
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public String getApplicationId() {
        return this.applicationId;
    }

    //utilities that act as a poor-man's closure & higher order functions - these help consolidate validation & construction of parameter keys
    private <R> R exec(Fun<R> fun, String namespaceCode, String componentCode, String parameterName) {
        if (StringUtils.isBlank(applicationId)) {
            throw new IllegalStateException("applicationId is blank - this service is not configured correctly");
        }

        return fun.f(ParameterKey.create(applicationId, namespaceCode, componentCode, parameterName));
    }

    private <R> R exec(Fun<R> fun, Class<?> componentClass, String parameterName) {
        return exec(fun, kualiModuleService.getNamespaceCode(componentClass), kualiModuleService.getComponentCode(componentClass), parameterName);
    }

    private interface Fun<R> {
        R f(ParameterKey key);
    }

    private void checkWatches() {
        Map<Consumer<Parameter>, Parameter> toNotify = new HashMap<>();
        synchronized (watchLock) {
            watches.keySet().forEach(parameterKey -> {
                String originalValue = watchedValues.get(parameterKey);
                Parameter newParameter = getParameter(parameterKey.getNamespaceCode(), parameterKey.getComponentCode(), parameterKey.getName());
                String newValue = nullSafeValue(newParameter);
                if (!Objects.equals(originalValue, newValue)) {
                    watchedValues.put(parameterKey, newValue);
                    watches.get(parameterKey).forEach(consumer -> toNotify.put(consumer, newParameter));
                }
            });
        }
        toNotify.keySet().forEach(consumer -> consumer.accept(toNotify.get(consumer)));
    }

    private String nullSafeValue(Parameter parameter) {
        return parameter == null ? null : parameter.getValue();
    }
}
