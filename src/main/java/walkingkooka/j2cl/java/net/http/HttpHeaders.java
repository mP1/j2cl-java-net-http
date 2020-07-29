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

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.BiPredicate;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

public final class HttpHeaders {

    public static HttpHeaders of(final Map<String, List<String>> source,
                                 final BiPredicate<String, String> filter) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(filter, "filter");

        final Map<String, List<String>> nameToValues = Maps.sorted(CASE_INSENSITIVE_ORDER);

        for (final Entry<String, List<String>> nameAndValue : source.entrySet()) {
            final String name = nameAndValue.getKey();
            if (CharSequences.isNullOrEmpty(name)) {
                throw new IllegalArgumentException("Header name cannot be null or empty");
            }

            final List<String> values = Lists.array();
            for (final String value : nameAndValue.getValue()) {
                if (null == value) {
                    throw new IllegalArgumentException("Header " + CharSequences.quote(name) + " value is null");
                }

                if (filter.test(name, value)) {
                    values.add(value.trim());
                }
            }

            if (false == values.isEmpty()) {
                if(null != nameToValues.put(name, Lists.readOnly(values))) {
                    throw new IllegalArgumentException("Duplicate eader " + CharSequences.quote(name) + " value is null");
                }
            }
        }

        return nameToValues.isEmpty() ?
                EMPTY :
                new HttpHeaders(Maps.readOnly(nameToValues));
    }

    private final static HttpHeaders EMPTY = new HttpHeaders(Maps.empty());

    private HttpHeaders(final Map<String, List<String>> nameToValues) {
        super();
        this.nameToValues = nameToValues;
    }

    public List<String> allValues(String name) {
        Objects.requireNonNull(name, "name");

        final List<String> values = map().get(name);
        return values != null ? values : List.of();
    }

    public Optional<String> firstValue(String name) {
        return allValues(name)
                .stream()
                .findFirst();
    }

    public OptionalLong firstValueAsLong(String name) {
        return allValues(name)
                .stream()
                .mapToLong(Long::valueOf)
                .findFirst();
    }

    public Map<String, List<String>> map() {
        return this.nameToValues;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.nameToValues.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof HttpHeaders && this.equals0((HttpHeaders) other);
    }

    private boolean equals0(final HttpHeaders other) {
        return this.map().equals(other.map());
    }

    private final Map<String, List<String>> nameToValues;

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .labelSeparator(": ");

        for (final Entry<String, List<String>> nameAndValue : this.nameToValues.entrySet()) {
            final String name = nameAndValue.getKey();
            for (final String value : nameAndValue.getValue()) {
                b.label(name)
                        .value(value)
                        .append('\n');
            }
        }

        return b.build();
    }
}
