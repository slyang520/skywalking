/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.skywalking.apm.toolkit.activation.trace;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.skywalking.apm.agent.core.plugin.match.MethodAnnotationMatch.byMethodAnnotationMatch;

/**
 * {@link TraceAnnotationActivation} enhance all method that annotated with <code>org.skywalking.apm.toolkit.trace.annotation.Trace</code>
 * by <code>org.skywalking.apm.toolkit.activation.trace.TraceAnnotationMethodInterceptor</code>.
 *
 * @author zhangxin
 */
public class TraceAnnotationActivation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String TRACE_ANNOTATION_METHOD_INTERCEPTOR = "org.skywalking.apm.toolkit.activation.trace.TraceAnnotationMethodInterceptor";
    public static final String TRACE_ANNOTATION = "org.skywalking.apm.toolkit.trace.Trace";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return isAnnotatedWith(named(TRACE_ANNOTATION));
                }

                @Override public String getMethodsInterceptor() {
                    return TRACE_ANNOTATION_METHOD_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return byMethodAnnotationMatch(new String[] {TRACE_ANNOTATION});
    }
}
