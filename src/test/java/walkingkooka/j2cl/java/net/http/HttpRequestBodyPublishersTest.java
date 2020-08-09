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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublisher;
import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublishers;
import walkingkooka.reflect.JavaVisibility;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpRequestBodyPublishersTest extends JavaNetHttpTestCase<BodyPublishers> {

    @Override
    public void testTestNaming() {
        // ignore
    }

    @Override
    public void testClassFinal() {
        // ignore
    }

    @Test
    public void testNoBody() {
        this.check(java.net.http.HttpRequest.BodyPublishers.noBody(),
                HttpRequest.BodyPublishers.noBody());
    }

    // ofString.........................................................................................................

    @Test
    public void testStringBodyNullStringFails() {
        final String body = null;

        assertThrows(NullPointerException.class, () -> java.net.http.HttpRequest.BodyPublishers.ofString(body));
        assertThrows(NullPointerException.class, () -> HttpRequest.BodyPublishers.ofString(body));
    }

    @Test
    public void testStringBodyEmpty() {
        this.ofStringAndCheck("");
    }

    @Test
    public void testStringBodyNotEmpty() {
        this.ofStringAndCheck("1");
    }

    @Test
    public void testStringBodyNotEmpty2() {
        this.ofStringAndCheck("1a");
    }

    @Test
    public void testStringBodyNotEmpty3() {
        this.ofStringAndCheck("1a2b3c");
    }

    private void ofStringAndCheck(final String body) {
        this.check(java.net.http.HttpRequest.BodyPublishers.ofString(body),
                HttpRequest.BodyPublishers.ofString(body));
    }

    // ofString Charset..................................................................................................

    @Test
    public void testStringBodyCharsetNullStringFails() {
        final String body = null;
        final Charset charset = Charset.defaultCharset();

        assertThrows(NullPointerException.class, () -> java.net.http.HttpRequest.BodyPublishers.ofString(body, charset));
        assertThrows(NullPointerException.class, () -> HttpRequest.BodyPublishers.ofString(body, charset));
    }

    @Test
    public void testStringBodyCharsetNullCharsetFails() {
        final String body = "";
        final Charset charset = null;

        assertThrows(NullPointerException.class, () -> java.net.http.HttpRequest.BodyPublishers.ofString(body, charset));
        assertThrows(NullPointerException.class, () -> HttpRequest.BodyPublishers.ofString(body, charset));
    }

    private final static Charset UTF16 = Charset.forName("UTF-16");

    @Test
    public void testStringBodyCharsetEmpty() {
        this.ofStringCharsetAndCheck("", UTF16);
    }

    @Test
    public void testStringBodyCharsetNotEmpty() {
        this.ofStringCharsetAndCheck("1", UTF16);
    }

    @Test
    public void testStringBodyCharsetNotEmpty2() {
        this.ofStringCharsetAndCheck("1a", UTF16);
    }

    @Test
    public void testStringBodyCharsetNotEmpty3() {
        this.ofStringCharsetAndCheck("1a2b3c", UTF16);
    }

    private void ofStringCharsetAndCheck(final String body, final Charset charset) {
        this.check(java.net.http.HttpRequest.BodyPublishers.ofString(body, charset),
                HttpRequest.BodyPublishers.ofString(body, charset));
    }

    // helpers..........................................................................................................

    private void check(final java.net.http.HttpRequest.BodyPublisher jre,
                       final BodyPublisher publisher) {
        final List<Object> jreEvents = Lists.array();
        jre.subscribe(subscriber(jreEvents));
        assertNotEquals(Lists.empty(), jreEvents, "jre " + jre);

        final List<Object> events = Lists.array();
        publisher.subscribe(subscriber(events));
        assertNotEquals(Lists.empty(), events, "" + publisher + " jre events: " + jreEvents);

        assertEquals(jreEvents, events);

        this.checkContentLength(jre, publisher);

        this.checkTwoSubscribers(jre, publisher);
    }

    private void checkTwoSubscribers(final java.net.http.HttpRequest.BodyPublisher jre,
                                     final BodyPublisher publisher) {
        final List<Object> jreEvents = Lists.array();
        jre.subscribe(subscriber(jreEvents));

        final List<Object> jreEvents2 = Lists.array();
        jre.subscribe(subscriber(jreEvents2));

        final List<Object> events = Lists.array();
        publisher.subscribe(subscriber(events));

        final List<Object> events2 = Lists.array();
        publisher.subscribe(subscriber(events2));

        assertEquals(jreEvents, events, () -> " events from 1st subscriber");
        assertEquals(jreEvents2, events2, () -> " events from 2nd subscriber");
    }


    private Subscriber<ByteBuffer> subscriber(final List<Object> events) {
        return new Subscriber<>() {
            @Override
            public void onSubscribe(final Subscription subscription) {
                events.add("onSubscribe");

                subscription.request(1);
            }

            @Override
            public void onNext(final ByteBuffer onNext) {
                events.add("onNext");
                events.add(new String(onNext.array(), Charset.defaultCharset()));
            }

            @Override
            public void onError(final Throwable throwable) {
                events.add("onError");
                events.add(throwable.getClass().getSimpleName() + "=" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                events.add("onComplete");
            }

            @Override
            public String toString() {
                return events.toString();
            }
        };
    }

    private void checkContentLength(final java.net.http.HttpRequest.BodyPublisher jre,
                                    final BodyPublisher publisher) {
        //assertEquals(jre.contentLength(), publisher.contentLength(), "contentLength");
    }

    @Override
    public Class<HttpRequest.BodyPublishers> type() {
        return HttpRequest.BodyPublishers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
