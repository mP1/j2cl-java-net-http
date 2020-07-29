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

import walkingkooka.j2cl.java.net.http.HttpClient.Redirect;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;

final class HttpClientBuilder implements HttpClient.Builder {

    static HttpClientBuilder empty() {
        return new HttpClientBuilder(null, null, Redirect.NEVER, 1, Version.HTTP_2);
    }

    private HttpClientBuilder(final Duration connectTimeout,
                              final Executor executor,
                              final Redirect followRedirects,
                              final int priority,
                              final Version version) {
        super();

        this.connectTimeout = connectTimeout;
        this.executor = executor;
        this.followRedirects = followRedirects;
        this.priority = priority;
        this.version = version;
    }

    @Override
    public HttpClientBuilder connectTimeout(final Duration connectTimeout) {
        Objects.requireNonNull(connectTimeout, "connectTimeout");

        this.connectTimeout = connectTimeout;
        return this;
    }

    Duration connectTimeout;

    @Override
    public HttpClientBuilder executor(final Executor executor) {
        Objects.requireNonNull(executor, "executor");

        this.executor = executor;
        return this;
    }

    Executor executor;

    @Override
    public HttpClientBuilder followRedirects(final Redirect followRedirects) {
        Objects.requireNonNull(followRedirects, "followRedirects");

        this.followRedirects = followRedirects;
        return this;
    }

    Redirect followRedirects;

    @Override
    public HttpClientBuilder priority(final int priority) {
        if (priority < 1 || priority > 256) {
            throw new IllegalArgumentException("Priority " + priority + " must be between 1 and 256");
        }

        this.priority = priority;
        return this;
    }

    int priority;

    @Override
    public HttpClientBuilder version(final Version version) {
        Objects.requireNonNull(version, "version");

        this.version = version;
        return this;
    }

    Version version;

    @Override
    public HttpClient build() {
        return HttpClientAjax.with(this.connectTimeout,
                this.executor,
                this.followRedirects,
                this.version);
    }
}
