/*
 * Copyright 2013 SpringSource
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
package grails.async

import grails.async.decorator.PromiseDecorator
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @author Graeme Rocher
 * @since 2.3
 */
class PromiseSpec extends Specification {

    void 'Test add promise decorator'() {
        
        when: 'a decorator is added'
            def decorator = { Closure c -> return { "*${c.call(*it)}*" } } as PromiseDecorator
            def p = Promises.createPromise({ 10 }, [decorator])
            def result = p.get()

        then: 'the result is decorated'
            result == '*10*'

    }
    
    void 'Test promise timeout handling'() {
        
        when: 'a promise that takes a while is created'
            def p = Promises.createPromise {
                sleep 1000
                println 'completed op'
            }
            p.get(100, TimeUnit.MILLISECONDS)

        then: 'a timeout error occurs'
            thrown TimeoutException

    }
    
    void 'Test promise map handling'() {
        
        when: 'a promise map is created'
            def map = Promises.createPromise(one: { 1 }, two: { 1 + 1 }, four: {2 * 2})
            def result = map.get() as Map

        then: 'the map is valid'
            result == [one: 1, two: 2, four: 4]
    }

    void 'Test long running task in list handling'() {

        when: 'a promise list is created from two promises'
            def p1 = Promises.createPromise {
                sleep 200
                1 + 1
            }
            def p2 = Promises.createPromise {
                sleep 200
                2 + 2
            }
            def list = Promises.createPromise(p1, p2)
            def result = null
            list.onComplete { result = it }

        then: 'the result is correct'
            new PollingConditions(timeout: 5).eventually {
                result == [2, 4]
            }
    }

    void 'Test promise list handling'() {
        
        when: 'a promise list is created from two promises'
            def p1 = Promises.createPromise { 1 + 1 }
            def p2 = Promises.createPromise { 2 + 2 }
            def list = Promises.createPromise(p1, p2)
            def result = null
            list.onComplete { result = it }

        then: 'the result is correct'
            new PollingConditions(timeout: 5).eventually {
                result == [2, 4]
            }

        when: 'a promise list is created from two closures'
            list = Promises.createPromise({ 3 + 3 }, { 4 + 4 })
            list.onComplete { result = it }

        then: 'the result is correct'
            new PollingConditions(timeout: 5).eventually {
                result == [6, 8]
            }
    }

    void 'Test promise onComplete handling'() {

        when: 'a promise is executed with an onComplete handler'
            def promise = Promises.createPromise { 1 + 1 }
            def result = null
            def hasError = false
            promise.onComplete { result = it }
            promise.onError { hasError = true }

        then: 'the onComplete handler is invoked and the onError handler is ignored'
            new PollingConditions(timeout: 5).eventually {
                result == 2
                hasError == false
            }
    }

    void 'Test promise onError handling'() {
        
        when: 'a promise is executed with an onComplete handler'
            def promise = Promises.createPromise { throw new RuntimeException('bad') }
            def result = null
            Throwable error = null
            promise.onComplete { result = it }
            promise.onError { error = it }

        then: 'the onComplete handler is invoked and the onError handler is ignored'
            new PollingConditions(timeout: 5).eventually {
                !result
                error
                error.message.contains('bad')
            }
    }

    void 'Test promise chaining'() {

        when: 'a promise is chained'
            def promise = Promises.createPromise { 1 + 1 }
            promise = promise.then { it * 2 } then { it + 6 }
            def result = promise.get()

        then: 'the chain is executed'
            result == 10
    }

    void 'Test promise chaining with exception'() {

        when: 'a promise is chained'
            def promise = Promises.createPromise { 1 + 1 }
            promise = promise.then { it * 2 } then { throw new RuntimeException('bad') } then { it + 6 }
            def result = promise.get()

        then: 'the chain is executed'
            thrown RuntimeException
            result == null
    }
}
