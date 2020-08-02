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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HttpResponseBodyHandlerTestCase2<S extends HttpResponseBodyHandler<T>, T> extends HttpResponseBodyHandlerTestCase<S, T>
        implements ToStringTesting<S> {

    final static Charset UTF8 = StandardCharsets.UTF_8;

    HttpResponseBodyHandlerTestCase2() {
        super();
    }

    @Test
    public final void testApplyNullResponseInfoFails() {
        assertThrows(NullPointerException.class, () -> this.createBodyHandler().apply(null));
    }

    abstract S createBodyHandler();
}
