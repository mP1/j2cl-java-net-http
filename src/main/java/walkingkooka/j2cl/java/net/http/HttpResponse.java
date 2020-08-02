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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public interface HttpResponse<T> {

    @FunctionalInterface
    interface BodyHandler<T> {
        BodySubscriber<T> apply(final ResponseInfo responseInfo);
    }

    public class BodyHandlers {
//        public static <T> BodyHandler<T> buffering(final BodyHandler<T> handler,
//                                                   final int bufferSize) {
//            throw new UnsupportedOperationException();
//        }

        public static BodyHandler<Void> discarding() {
            return HttpResponseBodyHandler.discarding();
        }

//        public static BodyHandler<Void> fromSubscriber(final Subscriber<? super List<ByteBuffer>> subscriber) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static <S extends Subscriber<? super List<ByteBuffer>>, T> BodyHandler<T> fromSubscriber(final S subscriber,
//                                                                                                        final Function<? super S, ? extends T> finisher) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodyHandler<Void> fromLineSubscriber(Subscriber<? super String> subscriber) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static <S extends Subscriber<? super String>, T> BodyHandler<T> fromLineSubscriber(final S subscriber,
//                                                                                                  final Function<? super S, ? extends T> finisher,
//                                                                                                  final String lineSeparator) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodyHandler<byte[]> ofByteArray() {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodyHandler<Void> ofByteArrayConsumer(final Consumer<Optional<byte[]>> consumer) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodyHandler<Stream<String>> ofLines() {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodyHandler<Flow.Publisher<List<ByteBuffer>>> ofPublisher() {
//            throw new UnsupportedOperationException();
//        }

        public static BodyHandler<String> ofString() {
            return ofString(UTF8);
        }

        private final static Charset UTF8 = Charset.forName("UTF-8");

        public static BodyHandler<String> ofString(final Charset charset) {
            return HttpResponseBodyHandler.ofString(charset);
        }
//
//        public static <U> BodyHandler<U> replacing(final U value) {
//            throw new UnsupportedOperationException();
//        }

        private BodyHandlers() {
        }
    }

    interface BodySubscriber<T>
            extends Flow.Subscriber<List<ByteBuffer>> {
        CompletionStage<T> getBody();
    }

    public class BodySubscribers {

        public static <T> BodySubscriber<T> buffering(final BodySubscriber<T> downstream,
                                                      final int bufferSize) {
            if (bufferSize <= 0) {
                throw new IllegalArgumentException("buffersize " + bufferSize + " must be greater than 0");
            }
            return downstream;
        }

        public static BodySubscriber<Void> discarding() {
            return HttpResponseBodySubscriber.discarding();
        }

//        public static BodySubscriber<Void> fromSubscriber(final Subscriber<? super List<ByteBuffer>> subscriber) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static <S extends Subscriber<? super List<ByteBuffer>>, T> BodySubscriber<T> fromSubscriber(final S subscriber,
//                                                                                                           final Function<? super S, ? extends T> finisher) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<Void> fromLineSubscriber(final Subscriber<? super String> subscriber) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static <S extends Subscriber<? super String>, T> BodySubscriber<T> fromLineSubscriber(final S subscriber,
//                                                                                                     final Function<? super S, ? extends T> finisher,
//                                                                                                     final Charset charset,
//                                                                                                     final String lineSeparator) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static <T, U> BodySubscriber<U> mapping(final BodySubscriber<T> upstream,
//                                                       final Function<? super T, ? extends U> mapper) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<byte[]> ofByteArray() {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<Void> ofByteArrayConsumer(final Consumer<Optional<byte[]>> consumer) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<InputStream> ofInputStream() {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<Stream<String>> ofLines(final Charset charset) {
//            throw new UnsupportedOperationException();
//        }
//
//        public static BodySubscriber<Publisher<List<ByteBuffer>>> ofPublisher() {
//            throw new UnsupportedOperationException();
//        }

        public static BodySubscriber<String> ofString(final Charset charset) {
            return HttpResponseBodySubscriber.ofString(charset);
        }

//        public static <U> BodySubscriber<U> replacing(final U value) {
//            throw new UnsupportedOperationException();
//        }

        private BodySubscribers() {
        }
    }

    interface ResponseInfo {
        HttpHeaders headers();

        int statusCode();

        HttpClient.Version version();
    }

    T body();

    HttpHeaders headers();

    Optional<HttpResponse<T>> previousResponse();

    HttpRequest request();

    int statusCode();

    URI uri();

    HttpClient.Version version();
}
