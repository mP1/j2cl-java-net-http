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
import java.util.concurrent.Flow.Subscriber;

final class HttpRequestPublishersBodyPublisherNoBody extends HttpRequestPublishersBodyPublisher {

    static HttpRequestPublishersBodyPublisherNoBody with() {
        return new HttpRequestPublishersBodyPublisherNoBody();
    }

    private HttpRequestPublishersBodyPublisherNoBody() {
        super();
    }

    @Override
    void onSubscribe(final Subscriber<? super ByteBuffer> subscriber) {
        subscriber.onComplete();
    }

    @Override
    String stringBody() {
        return "";
    }
}
