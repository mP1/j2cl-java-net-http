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

import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Flow;

public interface HttpRequest {

    interface Builder {
        Builder copy();

        Builder DELETE();

        Builder expectContinue(final boolean enable);

        Builder GET();

        Builder header(final String name,
                       final String value);

        Builder headers(final String... headers);

        Builder method(final String method,
                       final BodyPublisher bodyPublisher);

        Builder POST(final BodyPublisher bodyPublisher);

        Builder PUT(final BodyPublisher bodyPublisher);

        Builder setHeader(final String name,
                          final String value);

        Builder timeout(final Duration duration);

        Builder uri(final URI uri);

        Builder version(final HttpClient.Version version);

        HttpRequest build();
    }

    interface BodyPublisher extends Flow.Publisher<ByteBuffer> {
        long contentLength();
    }

    class BodyPublishers {
        public static BodyPublisher fromPublisher(final Flow.Publisher<? extends ByteBuffer> publisher) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher fromPublisher(final Flow.Publisher<? extends ByteBuffer> publisher,
                                                  final long contentLength) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher noBody() {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher ofByteArray(final byte[] buf) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher ofByteArray(final byte[] buf,
                                                final int offset,
                                                final int length) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher ofByteArrays(final Iterable<byte[]> iter) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher ofString(final String s) {
            throw new UnsupportedOperationException();
        }

        public static BodyPublisher ofString(final String s,
                                             final Charset charset) {
            throw new UnsupportedOperationException();
        }

        private BodyPublishers() {
        }
    }

    static Builder newBuilder() {
        throw new UnsupportedOperationException();
    }

    static Builder newBuilder(final URI uri) {
        throw new UnsupportedOperationException();
    }

    Optional<BodyPublisher> bodyPublisher();

    boolean expectContinue();

    HttpHeaders headers();

    String method();

    Optional<Duration> timeout();

    URI uri();

    Optional<HttpClient.Version> version();
}
