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

package org.skywalking.apm.plugin.spring.resttemplate.async.define;

import java.net.URI;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.skywalking.apm.agent.core.context.ContextSnapshot;
import org.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.skywalking.apm.plugin.spring.resttemplate.async.ResponseCallBackInterceptor;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * {@link ResponseExtractorFutureInstrumentation} enhance the <code>addCallback</code> method and <code>getDefault</code>
 * method of <code>org.springframework.web.client.AsyncRestTemplate$ResponseExtractorFuture</code> by
 * <code>org.skywalking.apm.plugin.spring.resttemplate.async.ResponseCallBackInterceptor</code> and
 * <code>org.skywalking.apm.plugin.spring.resttemplate.async.FutureGetInterceptor</code>.
 *
 * {@link ResponseCallBackInterceptor} set the {@link URI} and {@link ContextSnapshot} to inherited
 * <code>org.springframework.util.concurrent.SuccessCallback</code> and <code>org.springframework.util.concurrent.FailureCallback</code>
 *
 * @author zhangxin
 */
public class ResponseExtractorFutureInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ADD_CALLBACK_METHOD_NAME = "addCallback";
    private static final String ADD_CALLBACK_INTERCEPTOR = "org.skywalking.apm.plugin.spring.resttemplate.async.ResponseCallBackInterceptor";
    private static final String ENHANCE_CLASS = "org.springframework.web.client.AsyncRestTemplate$ResponseExtractorFuture";
    private static final String GET_METHOD_INTERCEPTOR = "org.skywalking.apm.plugin.spring.resttemplate.async.FutureGetInterceptor";
    private static final String GET_METHOD_NAME = "get";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];

    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(ADD_CALLBACK_METHOD_NAME);
                }

                @Override public String getMethodsInterceptor() {
                    return ADD_CALLBACK_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            },
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(GET_METHOD_NAME);
                }

                @Override public String getMethodsInterceptor() {
                    return GET_METHOD_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }
}
