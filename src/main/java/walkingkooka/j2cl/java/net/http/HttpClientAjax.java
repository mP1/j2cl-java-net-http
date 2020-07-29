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

import walkingkooka.j2cl.java.net.http.HttpResponse.BodyHandler;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;

final class HttpClientAjax extends HttpClient {

    static HttpClientAjax with(final Duration connectTimeout,
                               final Executor executor,
                               final HttpClient.Redirect followRedirects,
                               final HttpClient.Version version) {
        return new HttpClientAjax(connectTimeout, executor, followRedirects, version);
    }

    private HttpClientAjax(final Duration connectTimeout,
                           final Executor executor,
                           final HttpClient.Redirect followRedirects,
                           final HttpClient.Version version) {
        super();

        this.connectTimeout = Optional.ofNullable(connectTimeout);
        this.executor = Optional.ofNullable(executor);
        this.followRedirects = followRedirects;
        this.version = version;
    }

    @Override
    public <T> HttpResponse<T> send(final HttpRequest request,
                                    final BodyHandler<T> handler) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(handler, "handler");

        throw new UnsupportedOperationException(); // TODO Elemental
    }

//    @Override
//    public <T> CompletableFuture<HttpResponse<T>> sendAsync(final HttpRequest request,
//                                                            final BodyHandler<T> responseBodyHandler) {
//        throw new UnsupportedOperationException(); // TODO Elemental
//    }

    @Override
    public Optional<Duration> connectTimeout() {
        return this.connectTimeout;
    }

    private final Optional<Duration> connectTimeout;

    @Override
    public Optional<Executor> executor() {
        return this.executor;
    }

    private final Optional<Executor> executor;

    @Override
    public Redirect followRedirects() {
        return this.followRedirects;
    }

    final HttpClient.Redirect followRedirects;

    @Override
    public Version version() {
        return this.version;
    }

    private final HttpClient.Version version;
}
