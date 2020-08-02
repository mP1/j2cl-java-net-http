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
import java.nio.charset.StandardCharsets;

/**
 * A {@link walkingkooka.j2cl.java.net.http.HttpResponse.BodySubscriber} that ignores the body.
 */
final class HttpResponseBodySubscriberDiscarding extends HttpResponseBodySubscriber<Void> {

    static HttpResponseBodySubscriberDiscarding with() {
        return new HttpResponseBodySubscriberDiscarding();
    }

    private HttpResponseBodySubscriberDiscarding() {
        super();
    }

    @Override
    Charset charset() {
        return UTF8;
    }

    private final static Charset UTF8 = StandardCharsets.UTF_8;

    @Override
    void onBody(final String body) {
    }
}
