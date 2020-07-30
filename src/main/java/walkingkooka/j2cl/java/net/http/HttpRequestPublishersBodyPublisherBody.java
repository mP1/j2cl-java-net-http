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

abstract class HttpRequestPublishersBodyPublisherBody extends HttpRequestPublishersBodyPublisher {

    HttpRequestPublishersBodyPublisherBody() {
        super();
    }

    @Override
    void onSubscribe(final Subscriber<? super ByteBuffer> subscriber) {
        if (this.contentLength() > 0) {
            this.onSubscribe0(subscriber);
        }
        subscriber.onComplete();
    }

    private void onSubscribe0(final Subscriber<? super ByteBuffer> subscriber) {
        if (subscriber instanceof HttpRequestPublishersBodyPublisherSubscriber) {
            this.onSubscribeHttpRequestPublishersBodyPublisherSubscriber((HttpRequestPublishersBodyPublisherSubscriber) subscriber);
        } else {
            subscriber.onNext(this.byteBufferBody());
        }
    }

    private void onSubscribeHttpRequestPublishersBodyPublisherSubscriber(final HttpRequestPublishersBodyPublisherSubscriber subscriber) {
        subscriber.onNextStringBody(this.stringBody());
    }

    abstract ByteBuffer byteBufferBody();
}
