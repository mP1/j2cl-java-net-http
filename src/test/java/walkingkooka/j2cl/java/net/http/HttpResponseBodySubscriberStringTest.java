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

import java.nio.ByteBuffer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpResponseBodySubscriberStringTest extends HttpResponseBodySubscriberTestCase2<HttpResponseBodySubscriberString, String> {

    @Test
    public void testOnNextByteBufferEmpty() throws Exception {
        this.onNextByteBufferAndCheck("");
    }

    @Test
    public void testOnNextByteBufferNotEmpty() throws Exception {
        this.onNextByteBufferAndCheck("1");
    }

    @Test
    public void testOnNextByteBufferNotEmpty2() throws Exception {
        this.onNextByteBufferAndCheck("abc123");
    }

    private void onNextByteBufferAndCheck(final String string) throws Exception {
        final java.net.http.HttpResponse.BodySubscriber<String> jre = java.net.http.HttpResponse.BodySubscribers.ofString(UTF8);
        final HttpResponseBodySubscriberString bodySubscriber = HttpResponseBodySubscriberString.with(UTF8);

        jre.onSubscribe(subscription(jre));
        bodySubscriber.onSubscribe(subscription(bodySubscriber));

        final List<ByteBuffer> buffers = Lists.of(ByteBuffer.wrap(string.getBytes(UTF8)));

        boolean pass = false;
        try {
            jre.onNext(buffers);
            pass = true;
        } catch (final AssertionError error) {
            assertThrows(error.getClass(), () -> bodySubscriber.onNext(buffers));
        }

        if (pass) {
            bodySubscriber.onNext(buffers);
        }

        jre.onComplete();
        bodySubscriber.onComplete();

        this.check(jre, bodySubscriber);
    }

    @Test
    public void testOnNextStringNotEmpty() throws Exception {
        this.onNextStringAndCheck("1");
    }

    @Test
    public void testOnStringNotEmpty2() throws Exception {
        this.onNextStringAndCheck("abc123");
    }

    private void onNextStringAndCheck(final String string) throws Exception {
        final java.net.http.HttpResponse.BodySubscriber<String> jre = java.net.http.HttpResponse.BodySubscribers.ofString(UTF8);
        final HttpResponseBodySubscriberString bodySubscriber = HttpResponseBodySubscriberString.with(UTF8);

        jre.onSubscribe(subscription(jre));
        bodySubscriber.onSubscribe(subscription(bodySubscriber));

        final List<ByteBuffer> buffers = Lists.of(ByteBuffer.wrap(string.getBytes(UTF8)));
        jre.onNext(buffers);
        bodySubscriber.onNextStringBody(string);

        jre.onComplete();
        bodySubscriber.onComplete();

        this.check(jre, bodySubscriber);
    }

    @Override
    HttpResponseBodySubscriberString createSubscriber() {
        return HttpResponseBodySubscriberString.with(UTF8);
    }

    @Override
    public Class<HttpResponseBodySubscriberString> type() {
        return HttpResponseBodySubscriberString.class;
    }
}
