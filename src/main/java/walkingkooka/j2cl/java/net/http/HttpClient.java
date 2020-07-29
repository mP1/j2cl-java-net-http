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

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * An emulated subset of {@link java.net.http.HttpClient}.
 */
public abstract class HttpClient {

    protected HttpClient() {
        super();
    }

    public interface Builder {
        HttpClient build();

        Builder connectTimeout(final Duration duration);

        Builder executor(final Executor executor);

        Builder followRedirects(final Redirect policy);

        // ignored.
        Builder priority(final int priority);

        Builder version(final Version version);
    }

    public enum Redirect {
        ALWAYS,
        NEVER,
        NORMAL
    }

    public enum Version {
        HTTP_1_1,
        HTTP_2
    }

    public static Builder newBuilder() {
        return HttpClientBuilder.empty();
    }

    public static HttpClient newHttpClient() {
        return newBuilder().build();
    }

    abstract public Optional<Duration> connectTimeout();

    abstract public Optional<Executor> executor();

    abstract public Redirect followRedirects();

    abstract public <T> HttpResponse<T> send(final HttpRequest request,
                                             final HttpResponse.BodyHandler<T> responseBodyHandler);

//    abstract public <T> CompletableFuture<HttpResponse<T>> sendAsync(final HttpRequest request,
//                                                                     final HttpResponse.BodyHandler<T> responseBodyHandler);
//
//    abstract public <T> CompletableFuture<HttpResponse<T>> sendAsync(final HttpRequest request,
//                                                                     final HttpResponse.BodyHandler<T> responseBodyHandler,
//                                                                     final HttpResponse.PushPromiseHandler<T> pushPromiseHandler); // TODO eliminate ???

    abstract public Version version();
}
