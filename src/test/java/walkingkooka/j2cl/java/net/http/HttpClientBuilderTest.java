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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpClientBuilderTest implements ClassTesting2<HttpClientBuilder> {

    @Test
    public void testConnectTimeoutFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpClient.newBuilder().connectTimeout(null));
        assertThrows(NullPointerException.class, () -> HttpClientBuilder.empty().connectTimeout(null));
    }

    @Test
    public void testConnectTimeout() {
        final Duration duration = Duration.ofMillis(100);
        java.net.http.HttpClient.newBuilder().connectTimeout(duration);

        HttpClientBuilder.empty()
                .connectTimeout(duration);
    }

    @Test
    public void testConnectTimeoutSame() {
        final Duration duration = Duration.ofMillis(100);
        java.net.http.HttpClient.newBuilder().connectTimeout(duration);

        final HttpClientBuilder b = HttpClientBuilder.empty()
                .connectTimeout(duration);
        assertSame(b, b.connectTimeout(duration));
    }

    @Test
    public void testExecutorFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpClient.newBuilder().executor(null));
        assertThrows(NullPointerException.class, () -> HttpClientBuilder.empty().executor(null));
    }

    @Test
    public void testExecutor() {
        final Executor executor = (r) -> {
        };
        java.net.http.HttpClient.newBuilder().executor(executor);

        HttpClientBuilder.empty()
                .executor(executor);
    }

    @Test
    public void testExecutorSame() {
        final Executor executor = (r) -> {
        };
        java.net.http.HttpClient.newBuilder().executor(executor);

        final HttpClientBuilder b = HttpClientBuilder.empty()
                .executor(executor);
        assertSame(b, b.executor(executor));
    }

    @Test
    public void testFollowRedirectsFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpClient.newBuilder().followRedirects(null));
        assertThrows(NullPointerException.class, () -> HttpClientBuilder.empty().followRedirects(null));
    }

    @Test
    public void testFollowRedirects() {
        java.net.http.HttpClient.newBuilder().followRedirects(java.net.http.HttpClient.Redirect.ALWAYS);

        HttpClientBuilder.empty()
                .followRedirects(Redirect.ALWAYS);
    }

    @Test
    public void testFollowRedirectSame() {
        java.net.http.HttpClient.newBuilder().followRedirects(java.net.http.HttpClient.Redirect.ALWAYS);

        final Redirect redirect = Redirect.ALWAYS;
        final HttpClientBuilder b = HttpClientBuilder.empty()
                .followRedirects(redirect);
        assertSame(b, b.followRedirects(redirect));
    }

    @Test
    public void testFollowRedirectDifferent() {
        java.net.http.HttpClient.newBuilder().followRedirects(java.net.http.HttpClient.Redirect.ALWAYS);

        final HttpClientBuilder b = HttpClientBuilder.empty()
                .followRedirects(Redirect.ALWAYS);
        assertNotSame(b, b.followRedirects(Redirect.NORMAL));
    }

    @Test
    public void testPriorityFails() {
        assertThrows(IllegalArgumentException.class, () -> java.net.http.HttpClient.newBuilder().priority(0));
        assertThrows(IllegalArgumentException.class, () -> HttpClientBuilder.empty().priority(0));
    }

    @Test
    public void testPriorityFails2() {
        assertThrows(IllegalArgumentException.class, () -> java.net.http.HttpClient.newBuilder().priority(257));
        assertThrows(IllegalArgumentException.class, () -> HttpClientBuilder.empty().priority(257));
    }

    @Test
    public void testPriority() {
        for (int i = 1; i <= 256; i++) {
            java.net.http.HttpClient.newBuilder().priority(i);

            HttpClientBuilder.empty()
                    .priority(i);
        }
    }

    @Test
    public void testPrioritySame() {
        final int priority = 1;
        java.net.http.HttpClient.newBuilder().priority(priority);

        final HttpClientBuilder b = HttpClientBuilder.empty()
                .priority(priority);
        assertSame(b, b.priority(priority));
    }

    @Test
    public void testVersionFails() {
        assertThrows(NullPointerException.class, () -> java.net.http.HttpClient.newBuilder().version(null));
        assertThrows(NullPointerException.class, () -> HttpClientBuilder.empty().version(null));
    }

    @Test
    public void testVersion() {
        java.net.http.HttpClient.newBuilder().version(java.net.http.HttpClient.Version.HTTP_1_1);

        HttpClientBuilder.empty()
                .version(Version.HTTP_1_1);
    }

    @Test
    public void testVersionSame() {
        java.net.http.HttpClient.newBuilder().version(java.net.http.HttpClient.Version.HTTP_1_1);

        final Version version = Version.HTTP_1_1;
        final HttpClientBuilder b = HttpClientBuilder.empty()
                .version(version);
        assertSame(b, b.version(version));
    }

    @Test
    public void testVersionDifferent() {
        java.net.http.HttpClient.newBuilder().version(java.net.http.HttpClient.Version.HTTP_1_1);

        final HttpClientBuilder b = HttpClientBuilder.empty()
                .version(Version.HTTP_1_1);
        assertNotSame(b, b.version(Version.HTTP_2));
    }

    // buildAndCheck............................................................................................................

    @Test
    public void testBuild() {
        buildAndCheck(Duration.ofMillis(500), (r) -> {
        }, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testBuildWithoutDuration() {
        buildAndCheck(null, (r) -> {
        }, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testBuildWithoutExecutor() {
        buildAndCheck(Duration.ofMillis(500), null, Redirect.ALWAYS, Version.HTTP_1_1);
    }

    @Test
    public void testBuildWithoutRedirect() {
        buildAndCheck(Duration.ofMillis(500), (r) -> {
        }, null, Version.HTTP_1_1);
    }

    @Test
    public void testBuildWithoutVersion() {
        buildAndCheck(Duration.ofMillis(500), (r) -> {
        }, Redirect.ALWAYS, null);
    }

    private void buildAndCheck(final Duration connectTimeout,
                               final Executor executor,
                               final HttpClient.Redirect followRedirects,
                               final HttpClient.Version version) {
        java.net.http.HttpClient jre = null;

        try {
            java.net.http.HttpClient.Builder b = java.net.http.HttpClient.newBuilder();
            if (null != connectTimeout) {
                b = b.connectTimeout(connectTimeout);
            }

            if (null != executor) {
                b = b.executor(executor);
            }

            if (null != followRedirects) {
                b = b.followRedirects(java.net.http.HttpClient.Redirect.valueOf(followRedirects.name()));
            }

            if (null != version) {
                b = b.version(java.net.http.HttpClient.Version.valueOf(version.name()));
            }

            jre = b.build();
        } catch (final Exception cause) {
            assertThrows(cause.getClass(), () -> HttpClientAjax.with(connectTimeout, executor, followRedirects, version));
        }

        if (null != jre) {
            HttpClient.Builder b = HttpClient.newBuilder();
            if (null != connectTimeout) {
                b = b.connectTimeout(connectTimeout);
            }

            if (null != executor) {
                b = b.executor(executor);
            }

            if (null != followRedirects) {
                b = b.followRedirects(HttpClient.Redirect.valueOf(followRedirects.name()));
            }

            if (null != version) {
                b = b.version(HttpClient.Version.valueOf(version.name()));
            }

            final Redirect followRedirectNeverNull = null == followRedirects ?
                    Redirect.NEVER :
                    followRedirects;

            final Version versionNeverNull = null == version ?
                    Version.HTTP_2 :
                    version;

            {
                final HttpClientAjax client = (HttpClientAjax) b.build();

                assertEquals(Optional.ofNullable(connectTimeout), client.connectTimeout());
                assertEquals(Optional.ofNullable(executor), client.executor());
                assertSame(followRedirectNeverNull, client.followRedirects());
                assertSame(versionNeverNull, client.version());

                assertEquals(jre.connectTimeout(), client.connectTimeout(), "connectTimeout");
                assertEquals(jre.executor(), client.executor(), "executor");
                checkEnum(jre.followRedirects(), client.followRedirects(), "followRedirects");
                checkEnum(jre.version(), client.version(), "version");
            }

            final HttpClientAjax client = HttpClientAjax.with(connectTimeout, executor, followRedirects, version);

            assertEquals(Optional.ofNullable(connectTimeout), client.connectTimeout());
            assertEquals(Optional.ofNullable(executor), client.executor());
            assertSame(followRedirects, client.followRedirects());
            assertSame(version, client.version());
        }
    }

    private static void checkEnum(final Enum<?> expected,
                                  final Enum<?> actual,
                                  final String message) {
        assertEquals(toString(expected), toString(actual), message);
    }

    private static String toString(final Enum<?> e) {
        return null == e ? null : e.name();
    }

    // ClassTesting2....................................................................................................

    @Override
    public Class<HttpClientBuilder> type() {
        return HttpClientBuilder.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
