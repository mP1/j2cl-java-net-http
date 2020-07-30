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
import walkingkooka.ToStringTesting;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;
import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublisher;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.Flow.Subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpRequestAjaxTest implements ClassTesting2<HttpRequestAjax>, ToStringTesting<HttpRequestAjax> {

    static class TestBodyPublisher implements java.net.http.HttpRequest.BodyPublisher, BodyPublisher {
        @Override
        public long contentLength() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subscribe(Subscriber<? super ByteBuffer> subscriber) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Custom BodyPublisher";
        }
    }

    private final static TestBodyPublisher PUBLISHER = new TestBodyPublisher();

    private final static String METHOD = "METHOD1";

    private final static java.net.URI URI = java.net.URI.create("http://example");
    private final static boolean EXPECT_CONTINUE = true;

    private final static String HEADER = "Header1";
    private final static String VALUE = "value1";

    private final static Duration TIMEOUT = Duration.ofMillis(501);
    private final static Version VERSION = Version.HTTP_2;
    private final static java.net.http.HttpClient.Version JRE_VERSION = java.net.http.HttpClient.Version.HTTP_2;

    @Test
    public void testMissingMissingURIFails() {
        assertThrows(IllegalStateException.class, () -> java.net.http.HttpRequest.newBuilder().build());
        assertThrows(IllegalStateException.class, () -> HttpRequest.newBuilder().build());
    }

    @Test
    public void testMissingOnlyUri() {
        this.check(java.net.http.HttpRequest.newBuilder(URI),
                HttpRequest.newBuilder(URI));
    }

    @Test
    public void testMissingMissingExpectContinue() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT)
                        .version(JRE_VERSION),
                HttpRequest.newBuilder(URI)
                        .method(METHOD, PUBLISHER)
                        .header(HEADER, VALUE)
                        .timeout(TIMEOUT)
                        .version(VERSION)
                        .expectContinue(true));
    }

    @Test
    public void testMissingMissingMethod() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .timeout(TIMEOUT)
                        .version(JRE_VERSION),
                HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .timeout(TIMEOUT)
                        .version(VERSION));
    }

    @Test
    public void testMissingHeaders() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT)
                        .version(JRE_VERSION),
                HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT)
                        .version(VERSION));
    }

    @Test
    public void testMissingTimeout() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .version(JRE_VERSION),
                HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .version(VERSION));
    }
    
    @Test
    public void testMissingMissingVersion() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT),
                HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT));
    }

    @Test
    public void testAll() {
        this.check(java.net.http.HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT)
                        .version(JRE_VERSION),
                HttpRequest.newBuilder(URI)
                        .expectContinue(EXPECT_CONTINUE)
                        .header(HEADER, VALUE)
                        .method(METHOD, PUBLISHER)
                        .timeout(TIMEOUT)
                        .version(VERSION));
    }


    private void check(final java.net.http.HttpRequest.Builder jre,
                       final HttpRequest.Builder b) {
        this.check(jre.build(), b.build());
    }

    private void check(final java.net.http.HttpRequest jre,
                       final HttpRequest b) {
        assertEquals(jre.bodyPublisher(), b.bodyPublisher(), "bodyPublisher"); // used instance implements both BodyPublisher interfaces
        assertEquals(jre.expectContinue(), b.expectContinue(), "expectContinue");
        assertEquals(jre.headers(), b.headers(), "headers");
        assertEquals(jre.method(), b.method(), "method");
        assertEquals(jre.timeout(), b.timeout(), "timeout");
        assertEquals(jre.version().map(Enum::name).orElse(""), b.version().map(Enum::name).orElse(""), "version");
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .POST(PUBLISHER)
                .setHeader("Content-type", "text/plain")
                .setHeader("Content-length", "1234")
                .expectContinue(true)
                .uri(URI)
                .version(Version.HTTP_2);
        this.toStringAndCheck(b.build().toString(), "POST http://example HTTP_2\n" +
                "Content-length: 1234\n" +
                "Content-type: text/plain\n" +
                "\n" +
                "expectContinue: true, Custom BodyPublisher");
    }

    @Test
    public void testToStringWithoutMethod() {
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .setHeader("Content-type", "text/plain")
                .setHeader("Content-length", "1234")
                .expectContinue(true)
                .uri(URI)
                .version(Version.HTTP_2);
        this.toStringAndCheck(b.build().toString(), "http://example HTTP_2\n" +
                "Content-length: 1234\n" +
                "Content-type: text/plain\n" +
                "\n" +
                "expectContinue: true");
    }

    @Test
    public void testToStringWithoutVersion() {
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .POST(PUBLISHER)
                .setHeader("Content-type", "text/plain")
                .setHeader("Content-length", "1234")
                .expectContinue(true)
                .uri(URI);
        this.toStringAndCheck(b.build().toString(), "POST http://example\n" +
                "Content-length: 1234\n" +
                "Content-type: text/plain\n" +
                "\n" +
                "expectContinue: true, Custom BodyPublisher");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HttpRequestAjax> type() {
        return HttpRequestAjax.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
