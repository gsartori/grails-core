/*
 * Copyright 2004-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.web.servlet.bootstrap;

import jakarta.servlet.ServletContext;

import groovy.lang.Closure;
import grails.core.GrailsClass;

/**
 * Loaded and executed on application load.
 *
 * @author Graeme Rocher
 */
public interface GrailsBootstrapClass extends GrailsClass {

    /**
     * Calls the init closure if one exists.
     */
    void callInit();

    /**
     * Calls the destroy closure if one exists.
     */
    void callDestroy();

    /**
     * Returns the init closure which is called on application load.
     *
     * @return A Closure instance
     */
    @SuppressWarnings("rawtypes")
    Closure getInitClosure();

    /**
     * Returns the destroy closure which is called on application exit.
     *
     * @return A Closure instance
     */
    @SuppressWarnings("rawtypes")
    Closure getDestroyClosure();
}
