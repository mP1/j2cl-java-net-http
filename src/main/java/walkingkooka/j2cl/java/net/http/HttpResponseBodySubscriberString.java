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

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * The preferred subscriber as no work is performed to return the XHR {@link String body}.
 */
final class HttpResponseBodySubscriberString extends HttpResponseBodySubscriber<String> {

    static HttpResponseBodySubscriberString with(final Charset charset) {
        Objects.requireNonNull(charset, "charset");
        return new HttpResponseBodySubscriberString(charset);
    }

    private HttpResponseBodySubscriberString(final Charset charset) {
        super();
        this.charset = charset;
    }

    @Override
    Charset charset() {
        return this.charset;
    }

    private final Charset charset;

    @Override
    void onBody(final String body) {
        this.value = body;
        assert !body.isEmpty() : "Body is empty";
    }
}
