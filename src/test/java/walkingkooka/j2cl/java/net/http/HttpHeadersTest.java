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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalLong;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpHeadersTest extends JavaNetHttpTestCase<HttpHeaders>
        implements HashCodeEqualsDefinedTesting2<HttpHeaders>,
        ToStringTesting<HttpHeaders> {

    private final static String HEADER = "Header1";
    private final static String VALUE = "Value2";
    private final static BiPredicate<String, String> FILTER = (h, n) -> true;
    private final static Map<String, List<String>> MAP = Maps.of(HEADER, Lists.of(VALUE));

    @Test
    public void testOfNullSourcesFails() {
        this.check(null, FILTER);
    }

    @Test
    public void testOfNullFilterFails() {
        this.check(Maps.of(HEADER, Lists.of(VALUE)), null);
    }

    @Test
    public void testOfNullValueFails() {
        this.check(Maps.of(HEADER, null), null);
    }

    @Test
    public void testOfDuplicateHeaderNameFails() {
        this.check(Maps.of(HEADER, Lists.of(VALUE), HEADER.toUpperCase(), Lists.of(VALUE)), null);
    }

    @Test
    public void testOfEmpty() {
        final HttpHeaders empty = this.check(Maps.empty(), FILTER);
        final HttpHeaders empty2 = this.check(Maps.empty(), FILTER);
        assertSame(empty, empty2);
    }

    @Test
    public void testOfEmptyBecauseFiltering() {
        final BiPredicate<String, String> filter = (h, n) -> false;

        final HttpHeaders empty = this.check(MAP, filter);
        final HttpHeaders empty2 = this.check(MAP, filter);
        assertSame(empty, empty2);
    }

    @Test
    public void testOfEmptyBecauseFiltering2() {
        final Map<String, List<String>> MAP = Maps.of(HEADER, Lists.of(VALUE), "Header2b", Lists.of("value2a", "value2b"));

        final BiPredicate<String, String> filter = (h, n) -> false;

        final HttpHeaders empty = this.check(MAP, filter);
        final HttpHeaders empty2 = this.check(MAP, filter);
        assertSame(empty, empty2);
    }

    @Test
    public void testOfManyValues() {
        this.check(Maps.of(HEADER, Lists.of(VALUE), "Header2b", Lists.of("value2a", "value2b")), FILTER);
    }

    @Test
    public void testOfManyValuesSomeFiltered() {
        this.check(Maps.of(HEADER, Lists.of(VALUE), "Header2b", Lists.of("value2a", "value2b")),
                (h, n) -> h.equals(HEADER));
    }

    private HttpHeaders check(final Map<String, List<String>> source,
                              final BiPredicate<String, String> filter) {
        java.net.http.HttpHeaders jre = null;
        try {
            jre = java.net.http.HttpHeaders.of(source, filter);
        } catch (final Exception cause) {
            assertThrows(cause.getClass(), () -> HttpHeaders.of(source, filter));
        }

        final HttpHeaders emul;
        if (null != jre) {
            emul = HttpHeaders.of(source, filter);

            for (final Entry<String, List<String>> nameAndValue : source.entrySet()) {
                final String name = nameAndValue.getKey();

                assertEquals(jre.firstValue(name),
                        emul.firstValue(name),
                        () -> "firstValue of " + name + " in " + source);

                OptionalLong asLong = null;
                try {
                    asLong = jre.firstValueAsLong(name);
                } catch (final Exception cause) {
                    assertThrows(cause.getClass(), () -> emul.firstValueAsLong(name));
                }
                if (null != asLong) {
                    assertEquals(asLong,
                            emul.firstValueAsLong(name),
                            () -> "firstValueAsLong of " + name + " in " + source);
                }

                assertEquals(jre.allValues(name),
                        emul.allValues(name),
                        () -> "allValues of " + name + " in " + source);

                assertEquals(jre.map(),
                        emul.map(),
                        () -> "map of " + source);

                final java.net.http.HttpHeaders jre2 = jre;
                assertThrows(UnsupportedOperationException.class, () -> jre2.map().put("a", Lists.of("1")));
                assertThrows(UnsupportedOperationException.class, () -> emul.map().put("a", Lists.of("1")));
            }
        } else {
            emul = null;
        }

        return emul;
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(HttpHeaders.of(Maps.empty(), FILTER), "");
    }

    @Test
    public void testToStringOneEntry() {
        this.toStringAndCheck(HttpHeaders.of(MAP, FILTER), "Header1: Value2\n");
    }

    @Test
    public void testToStringOneEntryManyValues() {
        this.toStringAndCheck(HttpHeaders.of(Maps.of(HEADER, Lists.of("abc", "def")), FILTER), "Header1: abc\nHeader1: def\n");
    }

    @Test
    public void testToStringManyHeaders() {
        this.toStringAndCheck(HttpHeaders.of(Maps.of(HEADER, Lists.of("abc"), "Header2", Lists.of("xyz")), FILTER), "Header1: abc\nHeader2: xyz\n");
    }

    @Test
    public void testToStringManyHeaders2() {
        this.toStringAndCheck(HttpHeaders.of(Maps.of(HEADER, Lists.of("abc"), "Header2", Lists.of("xyz", "mno")), FILTER), "Header1: abc\nHeader2: xyz\nHeader2: mno\n");
    }

    // equals...........................................................................................................

    @Test
    public void testSameHeaderDifferentCase() {
        this.checkEquals(HttpHeaders.of(Maps.of(HEADER.toUpperCase(), Lists.of(VALUE)), FILTER));
    }

    @Test
    public void testDifferentHeader() {
        this.checkNotEquals(HttpHeaders.of(Maps.of("different", Lists.of(VALUE)), FILTER));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(HttpHeaders.of(Maps.of(HEADER, Lists.of("different")), FILTER));
    }

    @Test
    public void testDifferentValueCase() {
        this.checkNotEquals(HttpHeaders.of(Maps.of(HEADER, Lists.of(VALUE.toUpperCase())), FILTER));
    }

    // ClassTesting2....................................................................................................

    @Override
    public Class<HttpHeaders> type() {
        return HttpHeaders.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public HttpHeaders createObject() {
        return HttpHeaders.of(Maps.of(HEADER, Lists.of(VALUE)), FILTER);
    }
}
