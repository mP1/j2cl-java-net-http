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

import walkingkooka.j2cl.java.net.http.HttpResponse.BodySubscriber;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscription;

/**
 * This class is intended to only be subclasses by {@link HttpClient} collaborators.
 */
abstract class HttpResponseBodySubscriber<T> implements BodySubscriber<T> {

    /**
     * {@see HttpResponseBodySubscriberDiscarding}
     */
    static HttpResponseBodySubscriberDiscarding discarding() {
        return HttpResponseBodySubscriberDiscarding.with();
    }

    /**
     * Creates a {@link HttpResponseBodySubscriberString}.
     */
    static HttpResponseBodySubscriberString ofString(final Charset charset) {
        return HttpResponseBodySubscriberString.with(charset);
    }

    HttpResponseBodySubscriber() {
        super();
    }

    // BodySubscriber...................................................................................................

    @Override
    public final CompletionStage<T> getBody() {
        return this.stage;
    }

    private final CompletableFuture<T> stage = new CompletableFuture<>();

    // Subscriber.......................................................................................................

    @Override
    public final void onSubscribe(final Subscription subscription) {
        Objects.requireNonNull(subscription, "subscription");

        subscription.request(1);
    }

    @Override
    public void onNext(final List<ByteBuffer> body) {
        Objects.requireNonNull(body, "body");

        body.forEach(this::onNext0);
    }

    /**
     * Assumes a single body part.
     */
    private void onNext0(final ByteBuffer body) {
        this.onNextStringBody(new String(body.array(), this.charset()));
    }

    /**
     * {@link Charset} used to transform the XHR body which is a {@link String} into bytes.
     */
    abstract Charset charset();

    /**
     * Because XHR deals with {@link String} and not bytes, this is the method that actually is invoked and does the work
     * of mapping the value as necessary.
     */
    final void onNextStringBody(final String body) {
        if (null != this.value) {
            throw new IllegalStateException("Multi-part responses not supported");
        }
        this.onBody(body);
    }

    /**
     * Transforms the body into the value eventually given to the {@link CompletionStage}.
     */
    abstract void onBody(final String body);

    @Override
    public final void onError(final Throwable throwable) {
        this.stage.completeExceptionally(throwable);
    }

    /**
     * Notifies the {@link CompletionStage} that the HTTP request has completed successfully returning the body.
     */
    @Override
    public final void onComplete() {
        this.stage.complete(this.value);
    }

    /**
     * Eventually the value that is delivered by {@link #onComplete()}.
     */
    T value;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.stage.toString();
    }

}
