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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;
import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublisher;
import walkingkooka.net.header.HttpHeaderName;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

final class HttpRequestBuilder implements HttpRequest.Builder {

    static HttpRequestBuilder empty() {
        return new HttpRequestBuilder(null, null, false, Maps.sorted(String.CASE_INSENSITIVE_ORDER), null, null, null);
    }

    private HttpRequestBuilder(final String method,
                               final BodyPublisher bodyPublisher,
                               final boolean expectContinue,
                               final Map<String, List<String>> headers,
                               final Duration timeout,
                               final URI uri,
                               final Version version) {
        super();

        this.method = method;
        this.bodyPublisher = bodyPublisher;
        this.expectContinue = expectContinue;
        this.headers = headers;
        this.timeout = timeout;
        this.uri = uri;
        this.version = version;
    }

    @Override
    public HttpRequestBuilder copy() {
        return new HttpRequestBuilder(this.method,
                this.bodyPublisher,
                this.expectContinue,
                this.headers,
                this.timeout,
                this.uri,
                this.version);
    }

    @Override
    public HttpRequestBuilder DELETE() {
        return this.method0("DELETE");
    }

    @Override
    public HttpRequestBuilder GET() {
        return this.method0("GET");
    }

    @Override
    public HttpRequestBuilder POST(final BodyPublisher bodyPublisher) {
        return this.method("POST", bodyPublisher);
    }

    @Override
    public HttpRequestBuilder PUT(final BodyPublisher bodyPublisher) {
        return this.method("PUT", bodyPublisher);
    }

    private HttpRequestBuilder method0(final String method) {
        return this.method(method, HttpRequest.BodyPublishers.noBody());
    }

    @Override
    public HttpRequestBuilder method(final String method,
                                     final BodyPublisher bodyPublisher) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(bodyPublisher, "bodyPublisher");

        this.bodyPublisher = bodyPublisher;
        this.method = method;
        return this;
    }

    String method;
    BodyPublisher bodyPublisher;

    @Override
    public HttpRequestBuilder expectContinue(final boolean enable) {
        this.expectContinue = enable;
        return this;
    }

    boolean expectContinue;

    @Override
    public HttpRequestBuilder header(final String name,
                                     final String value) {
        checkHeader(name, value);

        List<String> values = this.headers.get(name);
        if (null == values) {
            values = Lists.array();
            this.headers.put(name, values);
        }
        values.add(value);
        return this;
    }

    @Override
    public HttpRequestBuilder headers(final String... headers) {
        Objects.requireNonNull(headers, "headers");

        HttpRequestBuilder b = this;

        for (int i = 0; i < headers.length; i += 2) {
            if (i + 1 >= headers.length) {
                throw new IllegalArgumentException("Last header missing value: " + Arrays.toString(headers));
            }
            b = b.header(headers[i], headers[i + 1]);
        }

        return b;
    }

    @Override
    public HttpRequestBuilder setHeader(final String name,
                                        final String value) {
        checkHeader(name, value);

        final List<String> values = Lists.array();
        values.add(value);

        this.headers.put(name, values);
        return this;
    }

    private static void checkHeader(final String name,
                                    final String value) {
        HttpHeaderName.with(name);
        Objects.requireNonNull(value, "value");
    }

    final Map<String, List<String>> headers;

    @Override
    public HttpRequestBuilder timeout(final Duration duration) {
        Objects.requireNonNull(duration, "duration");

        this.timeout = duration;
        return this;
    }

    Duration timeout;

    @Override
    public HttpRequestBuilder uri(final URI uri) {
        Objects.requireNonNull(uri, "uri");

        this.uri = uri;
        return this;
    }

    URI uri;

    @Override
    public HttpRequestBuilder version(final Version version) {
        Objects.requireNonNull(version, "version");

        this.version = version;
        return this;
    }

    Version version;

    @Override
    public HttpRequestAjax build() {
        return HttpRequestAjax.with(Optional.ofNullable(this.bodyPublisher),
                this.expectContinue,
                HttpHeaders.of(this.headers, KEEP_ALL_HEADERS),
                this.method,
                Optional.ofNullable(this.timeout),
                this.uri,
                Optional.ofNullable(this.version));
    }

    private final static BiPredicate<String, String> KEEP_ALL_HEADERS = (n, v) -> true;

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

        this.headers.forEach((h, v) -> {
            b.labelSeparator(": ")
                    .separator("\n");
            for (final String value : v) {
                b.label(h)
                        .value(value);
            }
        });

        return b.separator("\n\n")
                .label("expectContinue").value(this.expectContinue)
                .separator(", ")
                .label("timeout").value(this.timeout)
                .value(this.bodyPublisher)
                .build();
    }
}
