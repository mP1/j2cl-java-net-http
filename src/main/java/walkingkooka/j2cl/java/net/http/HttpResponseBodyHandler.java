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
import walkingkooka.j2cl.java.net.http.HttpResponse.BodySubscriber;
import walkingkooka.j2cl.java.net.http.HttpResponse.ResponseInfo;

import java.nio.charset.Charset;
import java.util.Objects;

abstract class HttpResponseBodyHandler<T> implements BodyHandler<T> {

    /**
     * {@see HttpResponseBodyHandlerDiscarding}
     */
    static HttpResponseBodyHandlerDiscarding discarding() {
        return HttpResponseBodyHandlerDiscarding.INSTANCE;
    }

    /**
     * {@see HttpResponseBodyHandlerString}
     */
    static HttpResponseBodyHandlerString ofString(final Charset charset) {
        return HttpResponseBodyHandlerString.with(charset);
    }

    HttpResponseBodyHandler() {
        super();
    }

    @Override
    public final BodySubscriber<T> apply(final ResponseInfo responseInfo) {
        Objects.requireNonNull(responseInfo, "responseInfo");
        return this.apply0(responseInfo);
    }

    abstract BodySubscriber<T> apply0(final ResponseInfo responseInfo);
}
