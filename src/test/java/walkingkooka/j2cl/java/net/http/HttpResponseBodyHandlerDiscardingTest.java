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
import walkingkooka.j2cl.java.net.http.HttpClient.Version;
import walkingkooka.j2cl.java.net.http.HttpResponse.ResponseInfo;

import java.net.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HttpResponseBodyHandlerDiscardingTest extends HttpResponseBodyHandlerTestCase2<HttpResponseBodyHandlerDiscarding, Void> {

    @Test
    public final void testApplyResponseInfo() {
        final HttpResponseBodyHandlerDiscarding handler = HttpResponseBodyHandlerDiscarding.INSTANCE;
        assertEquals(HttpResponseBodySubscriberDiscarding.class,
                handler.apply(new ResponseInfo() {
                    @Override
                    public HttpHeaders headers() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public int statusCode() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Version version() {
                        throw new UnsupportedOperationException();
                    }
                }).getClass());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(HttpResponseBodyHandlerDiscarding.INSTANCE, "HttpResponse.discarding");
    }

    @Override
    HttpResponseBodyHandlerDiscarding createBodyHandler() {
        return HttpResponseBodyHandlerDiscarding.INSTANCE;
    }

    @Override
    public Class<HttpResponseBodyHandlerDiscarding> type() {
        return HttpResponseBodyHandlerDiscarding.class;
    }
}
