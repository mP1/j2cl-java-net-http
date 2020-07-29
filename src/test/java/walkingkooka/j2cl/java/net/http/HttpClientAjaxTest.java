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
import walkingkooka.j2cl.java.net.http.HttpClient.Redirect;
import walkingkooka.j2cl.java.net.http.HttpClient.Version;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class HttpClientAjaxTest implements ClassTesting2<HttpClientAjax> {

    @Test
    public void testWith() {
        with(Duration.ofMillis(500), (r) -> {
        }, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testWithoutDuration() {
        with(null, (r) -> {
        }, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testWithoutExecutor() {
        with(Duration.ofMillis(500), null, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testWithoutRedirect() {
        with(Duration.ofMillis(500), (r) -> {
        }, null, Version.HTTP_1_1);
    }

    @Test
    public void testWithoutVersion() {
        with(Duration.ofMillis(500), (r) -> {
        }, Redirect.ALWAYS, null);
    }

    private void with(final Duration connectTimeout,
                      final Executor executor,
                      final HttpClient.Redirect followRedirects,
                      final HttpClient.Version version) {
        final HttpClientAjax client = HttpClientAjax.with(connectTimeout, executor, followRedirects, version);
        assertEquals(Optional.ofNullable(connectTimeout), client.connectTimeout());
        assertEquals(Optional.ofNullable(executor), client.executor());
        assertSame(followRedirects, client.followRedirects());
        assertSame(version, client.version());
    }

    // ClassTesting2....................................................................................................

    @Override
    public Class<HttpClientAjax> type() {
        return HttpClientAjax.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
