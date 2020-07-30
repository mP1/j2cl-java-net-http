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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;

public abstract class HttpRequestPublishersBodyPublisherTestCase2<P extends HttpRequestPublishersBodyPublisher> extends HttpRequestPublishersBodyPublisherTestCase<P>
        implements ToStringTesting<P> {

    HttpRequestPublishersBodyPublisherTestCase2() {
        super();
    }

    @Test
    public void testToString() {
        final P publisher = this.createPublisher();
        this.toStringAndCheck(publisher.toString(), publisher.stringBody());
    }

    abstract P createPublisher();
}
