package org.hibersap.configuration;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 * 
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.mapping.model.BapiMapping;
import org.hibersap.session.ExecutionInterceptor;
import org.hibersap.session.SapErrorInterceptor;
import org.hibersap.session.SessionManagerImplementor;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class ConfigurationTest {
	private static final int NUMBER_OF_PROPERTIES_IN_TEST_CONFIGURATION = 7;
	private Configuration configuration;

	@Before
	@SuppressWarnings("serial")
	public void createConfiguration() {
		configuration = new Configuration() {
			// nothing to overwrite
		};
		configuration.getSessionManagerConfig().setContext(
				DummyContext.class.getName());
	}

	@Test
	public void addInterceptor() {
		final ExecutionInterceptor dummyInterceptor = new ExecutionInterceptor() {
			public void afterExecute(BapiMapping bapiMapping,
					Map<String, Object> functionMap) {
				// dummy
			}

			public void beforeExecute(BapiMapping bapiMapping,
					Map<String, Object> functionMap) {
				// dummy
			}
		};
		configuration.addInterceptor(dummyInterceptor);
		final SessionManagerImplementor sessionManager = (SessionManagerImplementor) configuration
				.buildSessionManager();

		assertTrue(sessionManager.getInterceptors().contains(dummyInterceptor));
	}

	@Test
	public void setGetOverwriteProperties() throws Exception {
		SessionManagerConfig sfConfig = configuration.getSessionManagerConfig();
		// overwrites context class
		assertEquals(DummyContext.class.getName(), sfConfig.getContext());
		sfConfig.setContext("test");
		assertEquals("test", sfConfig.getContext());

		// overwrites property
		assertEquals("user", sfConfig.getProperty("jco.client.user"));
		sfConfig.setProperty("jco.client.user", "test");
		assertEquals("test", sfConfig.getProperty("jco.client.user"));

		// overwrites whole configuration
		assertEquals(NUMBER_OF_PROPERTIES_IN_TEST_CONFIGURATION, sfConfig
				.getProperties().size());
		final SessionManagerConfig config = new SessionManagerConfig()
				.setProperty("testkey", "testvalue");
		configuration.setSessionManagerConfig(config);
		sfConfig = configuration.getSessionManagerConfig();
		assertEquals(1, sfConfig.getProperties().size());
		assertEquals("testvalue", sfConfig.getProperty("testkey"));
	}

	@Test
	public void settingsInitialized() {
		final SessionManagerImplementor sessionManager = (SessionManagerImplementor) configuration
				.buildSessionManager();
		final Settings settings = sessionManager.getSettings();

		// Context class
		assertEquals(DummyContext.class, settings.getContext().getClass());
	}

	@Test
	public void standardInterceptorsInitialized() {
		final SessionManagerImplementor sessionManager = (SessionManagerImplementor) configuration
				.buildSessionManager();
		final List<ExecutionInterceptor> interceptors = sessionManager
				.getInterceptors();
		assertEquals(1, interceptors.size());
		assertEquals(SapErrorInterceptor.class, interceptors.get(0).getClass());
	}
}
