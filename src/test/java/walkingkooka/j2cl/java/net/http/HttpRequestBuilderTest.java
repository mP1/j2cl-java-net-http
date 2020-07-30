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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;
import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublisher;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.Flow.Subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpRequestBuilderTest implements ClassTesting2<HttpRequestBuilder>,
        ToStringTesting<HttpRequestBuilder> {

    private final static URI URI = java.net.URI.create("http://example");

    private final static BodyPublisher PUBLISHER = new BodyPublisher() {
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
    };

    // empty............................................................................................................

    @Test
    public void testEmpty() {
        final HttpRequestBuilder b = HttpRequestBuilder.empty();

        assertEquals(false, b.expectContinue, "expectContinue");
        assertEquals(null, b.method, "method");
        assertEquals(null, b.bodyPublisher, "bodyPublisher");
        assertEquals(null, b.timeout, "timeout");
        assertEquals(null, b.version, "version");
    }

    // copy.............................................................................................................

    @Test
    public void testCopy() {
        final boolean expect = false;
        final String method = "METHOD1";
        final Duration timeout = Duration.ofMillis(501);
        final Version version = Version.HTTP_1_1;

        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .expectContinue(expect)
                .method(method, PUBLISHER)
                .timeout(timeout)
                .version(version);
        final HttpRequestBuilder copy = b.copy();
        assertNotSame(b, copy);

        assertEquals(b.expectContinue, copy.expectContinue, "expectContinue");
        assertEquals(b.method, copy.method, "method");
        assertEquals(b.bodyPublisher, copy.bodyPublisher, "bodyPublisher");
        assertEquals(b.timeout, copy.timeout, "timeout");
        assertEquals(b.version, copy.version, "version");
    }

    // method.............................................................................................................

    @Test
    public void testDelete() {
        this.checkMethodAndBodyPublisher(HttpRequestBuilder.empty().DELETE(), "DELETE");
    }

    @Test
    public void testGet() {
        this.checkMethodAndBodyPublisher(HttpRequestBuilder.empty().GET(), "GET");
    }

    @Test
    public void testPost() {
        this.checkMethodAndBodyPublisher(HttpRequestBuilder.empty().POST(PUBLISHER), "POST", PUBLISHER);
    }

    @Test
    public void testPut() {
        this.checkMethodAndBodyPublisher(HttpRequestBuilder.empty().PUT(PUBLISHER), "PUT", PUBLISHER);
    }

    @Test
    public void testMethod() {
        final String method = "Method123";
        this.checkMethodAndBodyPublisher(HttpRequestBuilder.empty().method(method, PUBLISHER), method, PUBLISHER);
    }

    private void checkMethodAndBodyPublisher(final HttpRequestBuilder b,
                                             final String method) {
        assertSame(method, b.method, "method");
    }

    private void checkMethodAndBodyPublisher(final HttpRequestBuilder b,
                                             final String method,
                                             final BodyPublisher bodyPublisher) {
        checkMethodAndBodyPublisher(b, method);
        assertEquals(bodyPublisher, b.bodyPublisher, "bodyPublisher");
    }

    // expectContinue..........................................................................................................

    @Test
    public void testExpectContinueFalse() {
        final boolean expect = false;
        java.net.http.HttpRequest.newBuilder().expectContinue(expect);

        HttpRequestBuilder.empty().expectContinue(expect);
    }

    @Test
    public void testExpectContinueTrue() {
        final boolean expect = true;
        java.net.http.HttpRequest.newBuilder().expectContinue(expect);

        HttpRequestBuilder.empty().expectContinue(expect);
    }

    @Test
    public void testExpectContinueSame() {
        final boolean expect = true;
        java.net.http.HttpRequest.newBuilder().expectContinue(expect);

        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .expectContinue(expect);
        assertSame(b, b.expectContinue(expect));
    }

    // timeout..........................................................................................................

    @Test
    public void testTimeoutFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpRequest.newBuilder().timeout(null));
        assertThrows(NullPointerException.class, () -> HttpRequestBuilder.empty().timeout(null));
    }

    @Test
    public void testTimeout() {
        final Duration duration = Duration.ofMillis(100);
        java.net.http.HttpRequest.newBuilder().timeout(duration);

        HttpRequestBuilder.empty().timeout(duration);
    }

    @Test
    public void testTimeoutSame() {
        final Duration duration = Duration.ofMillis(100);
        java.net.http.HttpRequest.newBuilder().timeout(duration);

        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .timeout(duration);
        assertSame(b, b.timeout(duration));
    }

    // version..........................................................................................................

    @Test
    public void testVersionFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpRequest.newBuilder().version(null));
        assertThrows(NullPointerException.class, () -> HttpRequestBuilder.empty().version(null));
    }

    @Test
    public void testVersion() {
        final Version version = Version.HTTP_1_1;
        java.net.http.HttpRequest.newBuilder().version(java.net.http.HttpClient.Version.valueOf(version.name()));

        HttpRequestBuilder.empty().version(version);
    }

    @Test
    public void testVersionSame() {
        final Version version = Version.HTTP_1_1;
        java.net.http.HttpRequest.newBuilder().version(java.net.http.HttpClient.Version.valueOf(version.name()));

        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .version(version);
        assertSame(b, b.version(version));
    }

    // header...........................................................................................................

    @Test
    public void testHeaderNullNameFails() {
        this.headerFails(null, "value1", NullPointerException.class);
    }

    @Test
    public void testHeaderNullValueFails() {
        this.headerFails("header1", null, NullPointerException.class);
    }

    private void headerFails(final String name,
                             final String value,
                             final Class<? extends Throwable> expected) {
        assertThrows(expected, () -> java.net.http.HttpRequest.newBuilder().header(name, value));
        assertThrows(expected, () -> HttpRequestBuilder.empty().header(name, value));
    }

    @Test
    public void testHeader() {
        final String name = "header1";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .header(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .header(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeaderInvalidName() {
        final String name = "9";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .header(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .header(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeaderSeveralDifferent() {
        final String name1 = "header1";
        final String value1 = "a1";

        final String name2 = "header2";
        final String value2 = "b2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .header(name1, value1)
                .header(name2, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .header(name1, value1)
                .header(name2, value2);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeaderSeveralValues() {
        final String name1 = "header1";
        final String value1 = "a1";
        final String value2 = "b1";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .header(name1, value1)
                .header(name1, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .header(name1, value1)
                .header(name1, value2);
        assertEquals(request.headers().map(), b.headers);
    }

    // header...........................................................................................................

    @Test
    public void testHeadersNullNameFails() {
        this.headersFails(NullPointerException.class, null, "value1");
    }

    @Test
    public void testHeadersNullValueFails() {
        this.headersFails(NullPointerException.class, "header1", null);
    }

    @Test
    public void testHeadersMissingValueFails() {
        this.headersFails(IllegalArgumentException.class, "header-missing-values");
    }

    private void headersFails(final Class<? extends Throwable> expected,
                              final String ...values) {
        assertThrows(expected, () -> java.net.http.HttpRequest.newBuilder(URI).headers(values));
        assertThrows(expected, () -> HttpRequestBuilder.empty().headers(values));
    }

    @Test
    public void testHeaders() {
        final String name = "header1";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .headers(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .headers(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeadersInvalidName() {
        final String name = "9";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .headers(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .headers(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeadersSeveralDifferent() {
        final String name1 = "header1";
        final String value1 = "a1";

        final String name2 = "header2";
        final String value2 = "b2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .headers(name1, value1, name2, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .headers(name1, value1)
                .headers(name2, value2);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testHeadersSeveralValues() {
        final String name1 = "header1";
        final String value1 = "a1";
        final String value2 = "b1";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .headers(name1, value1, name1, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .headers(name1, value1, name1, value2);
        assertEquals(request.headers().map(), b.headers);
    }

    // setHeader.........................................................................................................

    @Test
    public void testSetHeaderNullNameFails() {
        this.setHeaderFails(null, "value1", NullPointerException.class);
    }

    @Test
    public void testSetHeaderNullValueFails() {
        this.setHeaderFails("header1", null, NullPointerException.class);
    }

    private void setHeaderFails(final String name,
                                final String value,
                                final Class<? extends Throwable> expected) {
        assertThrows(expected, () -> java.net.http.HttpRequest.newBuilder(URI).header(name, value));
        assertThrows(expected, () -> HttpRequestBuilder.empty().setHeader(name, value));
    }

    @Test
    public void testSetHeader() {
        final String name = "header1";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .setHeader(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .setHeader(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testSetHeaderInvalidHeader() {
        final String name = "9";
        final String value = "value2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .setHeader(name, value)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .setHeader(name, value);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testSetHeaderSeveralDifferent() {
        final String name1 = "header1";
        final String value1 = "a1";

        final String name2 = "header2";
        final String value2 = "b2";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .setHeader(name1, value1)
                .setHeader(name2, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .setHeader(name1, value1)
                .setHeader(name2, value2);
        assertEquals(request.headers().map(), b.headers);
    }

    @Test
    public void testSetHeaderReplace() {
        final String name1 = "header1";
        final String value1 = "a1";
        final String value2 = "b1";

        final java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI)
                .setHeader(name1, value1)
                .setHeader(name1, value2)
                .build();
        final HttpRequestBuilder b = HttpRequestBuilder.empty()
                .setHeader(name1, value1)
                .setHeader(name1, value2);
        assertEquals(request.headers().map(), b.headers);
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
        this.toStringAndCheck(b.toString(), "POST http://example HTTP_2\n" +
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
        this.toStringAndCheck(b.toString(), "http://example HTTP_2\n" +
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
        this.toStringAndCheck(b.toString(), "POST http://example\n" +
                "Content-length: 1234\n" +
                "Content-type: text/plain\n" +
                "\n" +
                "expectContinue: true, Custom BodyPublisher");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HttpRequestBuilder> type() {
        return HttpRequestBuilder.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
