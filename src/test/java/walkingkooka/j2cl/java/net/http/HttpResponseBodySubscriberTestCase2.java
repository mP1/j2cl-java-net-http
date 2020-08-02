/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package walkingkooka.j2cl.java.net.http;

import walkingkooka.j2cl.java.net.http.HttpResponse.BodySubscriber;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HttpResponseBodySubscriberTestCase2<S extends HttpResponseBodySubscriber<T>, T> extends HttpResponseBodySubscriberTestCase<S> {

    final static Charset UTF8 = StandardCharsets.UTF_8;

    HttpResponseBodySubscriberTestCase2() {
        super();
    }

    // helpers..........................................................................................................

    final static <T> Subscription subscription(final Subscriber<T> subscriber) {
        return new Subscription() {
            @Override
            public void request(final long n) {
//                subscriber.onSubscribe(this);
            }

            @Override
            public void cancel() {

            }
        };
    }

    final void check(final java.net.http.HttpResponse.BodySubscriber jre,
                     final BodySubscriber subscriber) throws Exception {
        final CompletionStage<T> jreStage = jre.getBody();
        assertNotEquals(null, jreStage);

        final CompletionStage<T> stage = subscriber.getBody();
        assertNotEquals(null, stage);

        final CompletableFuture<T> jreFuture = jreStage.toCompletableFuture();
        assertEquals(true, jreFuture.isDone() | jreFuture.isCompletedExceptionally() | jreFuture.isCancelled(), () -> "jreFuture not completed in any way: " + jreFuture);

        final CompletableFuture<T> future = stage.toCompletableFuture();
        assertEquals(true, future.isDone() | future.isCompletedExceptionally() | future.isCancelled(), () -> "future not completed in any way: " + future);

        assertEquals(jreFuture.isDone(), future.isDone(), () -> "isDone jre: " + jreStage + " " + stage);
        assertEquals(jreFuture.isCompletedExceptionally(), future.isCompletedExceptionally(), () -> "isCompletedExceptionally jre: " + jreStage + " " + stage);
        assertEquals(jreFuture.isCancelled(), future.isCancelled(), () -> "isCancelled jre: " + jreStage + " " + stage);

        T expectedValue;
        try {
            expectedValue = jreFuture.get(500, TimeUnit.MICROSECONDS);
        } catch (final Exception failed) {
            expectedValue = null;

            assertThrows(failed.getClass(), () -> future.get(500, TimeUnit.MICROSECONDS), () -> "get " + jreStage + " " + stage);
        }
        if (null != expectedValue) {
            assertEquals(expectedValue, future.get(), () -> "get " + jreStage + " " + stage);
        }
    }
}
