/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.ge.conventions.gradle;

import java.net.URI;

import org.gradle.api.Action;
import org.gradle.caching.BuildCacheServiceFactory;
import org.gradle.caching.configuration.BuildCache;
import org.gradle.caching.configuration.BuildCacheConfiguration;
import org.gradle.caching.http.HttpBuildCache;
import org.gradle.caching.local.DirectoryBuildCache;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GradleConfigurableBuildCache}.
 *
 * @author Andy Wilkinson
 */
class GradleConfigurableBuildCacheTests {

	private final TestBuildCacheConfiguration configuration = new TestBuildCacheConfiguration();

	private final GradleConfigurableBuildCache buildCache = new GradleConfigurableBuildCache(this.configuration);

	@Test
	void localCacheCanBeEnabled() {
		this.buildCache.local((local) -> local.enable());
		assertThat(this.configuration.local.isEnabled()).isTrue();
	}

	@Test
	void remoteCacheCanBeEnabled() {
		this.buildCache.remote((remote) -> remote.enable());
		assertThat(this.configuration.remote.isEnabled()).isTrue();
	}

	@Test
	void pushingToRemoteCacheCanBeEnabled() {
		this.buildCache.remote((remote) -> remote.enablePush());
		assertThat(this.configuration.remote.isPush()).isTrue();
	}

	@Test
	void remoteCacheUriCanBeConfigured() {
		this.buildCache.remote((remote) -> remote.setUri(URI.create("https://cache.example.com/")));
		assertThat(this.configuration.remote.getUrl()).isEqualTo(URI.create("https://cache.example.com/"));
	}

	@Test
	void remoteCacheCredentialsCanBeConfigured() {
		this.buildCache.remote((remote) -> remote.setCredentials("alice", "secret"));
		assertThat(this.configuration.remote.getCredentials().getUsername()).isEqualTo("alice");
		assertThat(this.configuration.remote.getCredentials().getPassword()).isEqualTo("secret");
	}

	private static final class TestBuildCacheConfiguration implements BuildCacheConfiguration {

		private final DirectoryBuildCache local = new DirectoryBuildCache();

		private final HttpBuildCache remote = new HttpBuildCache();

		@Override
		public DirectoryBuildCache getLocal() {
			return this.local;
		}

		@Override
		public BuildCache getRemote() {
			throw new UnsupportedOperationException();
		}

		@Override
		@Deprecated
		public <T extends DirectoryBuildCache> T local(Class<T> cacheType) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void local(Action<? super DirectoryBuildCache> cacheType) {
			throw new UnsupportedOperationException();
		}

		@Override
		@Deprecated
		public <T extends DirectoryBuildCache> T local(Class<T> cacheType, Action<? super T> action) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T extends BuildCache> void registerBuildCacheService(Class<T> cacheType,
				Class<? extends BuildCacheServiceFactory<? super T>> factory) {
			throw new UnsupportedOperationException();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T extends BuildCache> T remote(Class<T> cacheType) {
			return (T) this.remote;
		}

		@Override
		public void remote(Action<? super BuildCache> action) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T extends BuildCache> T remote(Class<T> type, Action<? super T> action) {
			throw new UnsupportedOperationException();
		}

	}

}
