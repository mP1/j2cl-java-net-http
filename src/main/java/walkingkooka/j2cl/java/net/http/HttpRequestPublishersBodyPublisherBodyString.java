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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

final class HttpRequestPublishersBodyPublisherBodyString extends HttpRequestPublishersBodyPublisherBody {

    static HttpRequestPublishersBodyPublisherBodyString with(final String body,
                                                             final Charset charset) {
        Objects.requireNonNull(body, "body");
        Objects.requireNonNull(charset, "charset");

        return new HttpRequestPublishersBodyPublisherBodyString(body, charset);
    }

    private HttpRequestPublishersBodyPublisherBodyString(final String body,
                                                         final Charset charset) {
        super();
        this.body = body;
        this.charset = charset;
    }

    @Override
    ByteBuffer byteBufferBody() {
        return ByteBuffer.wrap(this.body.getBytes(this.charset));
    }

    @Override
    String stringBody() {
        return this.body;
    }

    final String body;
    final Charset charset;
}
