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

import java.util.concurrent.Flow.Subscription;

final class HttpRequestPublishersBodyPublisherSubscription implements Subscription {

    static HttpRequestPublishersBodyPublisherSubscription with(final HttpRequestPublishersBodyPublisher publisher) {
        return new HttpRequestPublishersBodyPublisherSubscription(publisher);
    }

    private HttpRequestPublishersBodyPublisherSubscription(final HttpRequestPublishersBodyPublisher publisher) {
        super();
        this.publisher = publisher;
    }

    @Override
    public void request(final long count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Invalid request count " + count);
        }
    }

    @Override
    public final void cancel() {
        if (false == this.cancelled) {
            this.cancelled = true;
            this.publisher.onCancel();
        }
    }

    /**
     * When true the subscription is cancelled.
     */
    boolean cancelled;

    private final HttpRequestPublishersBodyPublisher publisher;
}
