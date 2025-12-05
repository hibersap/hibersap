/*
 * Copyright (c) 2008-2025 tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.ejb.interceptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import static java.lang.String.format;
import static org.hibersap.ejb.util.ReflectionUtil.getHibersapSessionFields;
import static org.hibersap.ejb.util.ReflectionUtil.getSessionManagerJndiName;
import static org.hibersap.ejb.util.ReflectionUtil.injectSessionIntoTarget;

public class HibersapSessionInterceptor {

    private static final String HIBERSAP_SESSION_PREFIX = "hibersap.session.";

    private static final Log LOGGER = LogFactory.getLog(HibersapSessionInterceptor.class);

    @AroundInvoke
    public Object injectSessionsIntoEjb(final InvocationContext ctx) throws Exception {
        Set<Field> sessionFields = getHibersapSessionFields(ctx.getTarget());

        Map<Session, String> sessionsCreated = new HashMap<>();
        try {
            for (Field sessionField : sessionFields) {
                String jndiName = getSessionManagerJndiName(sessionField);
                String key = HIBERSAP_SESSION_PREFIX + jndiName;
                Session session = (Session) ctx.getContextData().get(key);

                if (session == null) {
                    LOGGER.debug("Create Hibersap session for SessionManager " + jndiName);
                    session = openSession(jndiName);
                    sessionsCreated.put(session, jndiName);
                    ctx.getContextData().put(key, session);
                }

                injectSessionIntoTarget(ctx.getTarget(), sessionField, session);
            }

            return ctx.proceed();
        } finally {
            closeSessions(sessionsCreated, ctx.getContextData());
        }
    }

    private void closeSessions(final Map<Session, String> sessions, final Map<String, Object> contextData) {
        Set<String> sessionManagerNamesWithError = new HashSet<>();
        for (Session session : sessions.keySet()) {
            String jndiName = sessions.get(session);
            try {
                contextData.remove(HIBERSAP_SESSION_PREFIX + jndiName);

                if (session != null && !session.isClosed()) {
                    LOGGER.debug("Close Hibersap session for SessionManager " + jndiName);
                    session.close();
                }
            } catch (RuntimeException e) {
                LOGGER.error("Error closing Hibersap Session for SessionManager with JNDI name " + jndiName, e);
                sessionManagerNamesWithError.add(jndiName);
            }
        }
        if (!sessionManagerNamesWithError.isEmpty()) {
            LOGGER.error(format(
                    "Error closing Session(s) for the SessionManager(s): %s. "
                            +
                            "The corresponding SAP connection may not be released! For individual reasons see error logs.",
                    sessionManagerNamesWithError
            ));
        }
    }

    private Session openSession(final String sessionManagerJndiName) {
        SessionManager sessionManager = lookupSessionManager(sessionManagerJndiName);
        return sessionManager.openSession();
    }

    private SessionManager lookupSessionManager(final String jndiName) {
        InitialContext context = null;
        try {
            context = new InitialContext();
            Object object = context.lookup(jndiName);

            if (object == null) {
                throw new HibersapException(
                        format("Lookup for JNDI name '%s' returned null. Expected to find an instance of %s",
                                jndiName, SessionManager.class.getName())
                );
            }
            if (!SessionManager.class.isAssignableFrom(object.getClass())) {
                throw new HibersapException(
                        format("Object bound under JNDI name '%s' is not a %s but an instance of %s",
                                jndiName, SessionManager.class.getName(), object.getClass().getName())
                );
            }
            return (SessionManager) object;
        } catch (NamingException e) {
            throw new HibersapException("Error creating InitialContext", e);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException e) {
                    LOGGER.warn("Error closing InitialContext", e);
                }
            }
        }
    }
}
