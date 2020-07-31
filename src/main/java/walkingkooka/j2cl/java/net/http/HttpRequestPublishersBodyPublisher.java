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

import walkingkooka.j2cl.java.net.http.HttpRequest.BodyPublisher;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.Flow.Subscriber;

/**
 * Base class for any of the {@link BodyPublisher} factory methods in {@link HttpRequest.BodyPublishers}
 */
abstract class HttpRequestPublishersBodyPublisher implements BodyPublisher {

    /**
     * {@see HttpRequestPublishersBodyPublisherNoBody}
     */
    static HttpRequestPublishersBodyPublisher noBody() {
        return HttpRequestPublishersBodyPublisherNoBody.with();
    }

    /**
     * {@see HttpRequestPublishersBodyPublisherBodyString}
     */
    static HttpRequestPublishersBodyPublisher stringBody(final String body,
                                                         final Charset charset) {
        return HttpRequestPublishersBodyPublisherBodyString.with(body, charset);
    }

    /**
     * Package private to keep sub classing limited.
     */
    HttpRequestPublishersBodyPublisher() {
        super();
    }

    // BodyPublisher....................................................................................................

    @Override
    public final long contentLength() {
        return this.stringBody().length();
    }

    @Override
    public final void subscribe(final Subscriber<? super ByteBuffer> subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        this.subscriber = subscriber;

        subscriber.onSubscribe(HttpRequestPublishersBodyPublisherSubscription.with(this));
        this.onSubscribe(subscriber);
    }

    abstract void onSubscribe(final Subscriber<? super ByteBuffer> subscriber);

    final void onCancel() {

    }

    /**
     * Prepare the content as a {@link String}.
     */
    abstract String stringBody();

    /**
     * Only one subscriber may be recorded.
     */
    private Subscriber<? super ByteBuffer> subscriber = null;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.stringBody();
    }
}
