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

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

final class HttpRequestAjax implements HttpRequest {

    static HttpRequestAjax with(final Optional<BodyPublisher> bodyPublisher,
                                final boolean expectContinue,
                                final HttpHeaders headers,
                                final String method,
                                final Optional<Duration> timeout,
                                final URI uri,
                                final Optional<Version> version) {
        if (null == uri) {
            throw new IllegalStateException("Missing uri");
        }

        return new HttpRequestAjax(bodyPublisher,
                expectContinue,
                headers,
                null == method ? "GET" : method, // default METHOD is GET
                timeout,
                uri,
                version);
    }

    private HttpRequestAjax(final Optional<BodyPublisher> bodyPublisher,
                            final boolean expectContinue,
                            final HttpHeaders headers,
                            final String method,
                            final Optional<Duration> timeout,
                            final URI uri,
                            final Optional<Version> version) {
        this.bodyPublisher = bodyPublisher;
        this.expectContinue = expectContinue;
        this.headers = headers;
        this.method = method;
        this.timeout = timeout;
        this.uri = uri;
        this.version = version;
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return bodyPublisher;
    }

    final Optional<BodyPublisher> bodyPublisher;

    @Override
    public boolean expectContinue() {
        return this.expectContinue;
    }

    final boolean expectContinue;

    @Override
    public HttpHeaders headers() {
        return this.headers;
    }

    final HttpHeaders headers;

    @Override
    public String method() {
        return this.method;
    }

    final String method;

    @Override
    public Optional<Duration> timeout() {
        return this.timeout;
    }

    final Optional<Duration> timeout;

    @Override
    public URI uri() {
        return this.uri;
    }

    final URI uri;

    @Override
    public Optional<Version> version() {
        return this.version;
    }

    final Optional<Version> version;

    // Object...........................................................................................................

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .valueSeparator(" ")
                .value(this.method)
                .value(this.uri)
                .value(this.version);

        if (null != this.method || null != this.uri || null != this.version) {
            b.append('\n');
        }

        b.value(this.headers);

        return b.separator("\n")
                .labelSeparator(": ")
                .label("expectContinue").value(this.expectContinue)
                .separator(", ")
                .label("timeout").value(this.timeout)
                .value(this.bodyPublisher)
                .build();
    }
}
