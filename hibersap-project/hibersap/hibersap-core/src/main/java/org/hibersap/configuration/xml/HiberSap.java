/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibersap.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@XmlRootElement(name = "hibersap", namespace = HiberSap.NAMESPACE)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class HiberSap {
	public final static String NAMESPACE = "urn:hibersap:hibersap-configuration:1.0";

	@XmlTransient
	private static Log LOG = LogFactory.getLog(HiberSap.class);

	@XmlTransient
	private static final long serialVersionUID = 1;

	private SessionFactoryConfig sessionFactory;

	public HiberSap() {
		LOG.trace("created");
	}

	public HiberSap(final SessionFactoryConfig sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSessionFactory(final SessionFactoryConfig sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@XmlElement(name = "session-factory", required = true, namespace = HiberSap.NAMESPACE)
	public SessionFactoryConfig getSessionFactory() {
		return sessionFactory;
	}
}
