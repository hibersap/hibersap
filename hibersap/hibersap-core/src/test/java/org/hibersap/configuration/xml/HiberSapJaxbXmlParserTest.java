package org.hibersap.configuration.xml;

/**
 * Copyright (C) 2008-2009 akquinet tech@spree GmbH
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

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class HiberSapJaxbXmlParserTest
{
    private static HibersapConfig config;
    private static SessionManagerConfig sessionManagerA12;
    private static SessionManagerConfig sessionManagerB34;

    @BeforeClass
    public static void createHibersapConfig()
    {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        config = hiberSapJaxbXmlParser.parseResource( "/xml-configurations/hibersapOK.xml" );
        sessionManagerA12 = config.getSessionManager( "A12" );
        sessionManagerB34 = config.getSessionManager( "B34" );
    }

    @Test
    public void createsTwoSessionManagers()
    {
        List<SessionManagerConfig> sessionManagers = config.getSessionManagers();

        assertThat( sessionManagers.size(), is( 2 ) );
    }

    @Test
    public void sessionManagersHaveCorrectNames()
    {
        final String a12Name = sessionManagerA12.getName();
        final String b34Name = sessionManagerB34.getName();

        assertThat( a12Name, is( "A12" ) );
        assertThat( b34Name, is( "B34" ) );
    }

    @Test
    public void sessionManagersHaveCorrectHibersapContext()
    {

        final String a12Context = sessionManagerA12.getContext();
        final String b34Context = sessionManagerB34.getContext();

        assertThat( a12Context, is( "org.hibersap.execution.jco.JCoContext" ) );
        assertThat( b34Context, is( "org.hibersap.execution.jca.JCAContext" ) );
    }

    @Test
    public void sessionManagerHasCorrectJcaConnectionFactory()
    {
        final String connectionSpecFactory = sessionManagerB34.getJcaConnectionFactory();

        assertThat( connectionSpecFactory, is( "java:/eis/sap/B34" ) );
    }

    @Test
    public void sessionManagerHasCorrectJcaConnectionSpecFactory()
    {
        final SessionManagerConfig manager = sessionManagerB34;

        assertThat( manager.getJcaConnectionSpecFactory(), is( "org.hibersap.test.MyTestConnectionSpecFactory" ) );
    }

    @Test
    public void sessionManagerHasCorrectProperties()
    {
        List<Property> properties = sessionManagerB34.getProperties();

        assertThat( properties.size(), is( 2 ) );
        assertThat( properties, hasItem( new Property( "property1_name", "property1_value" ) ) );
        assertThat( properties, hasItem( new Property( "property2_name", "property2_value" ) ) );
    }

    @Test
    public void sessionManagerHasCorrectAnnotatedClasses()
    {
        final List<String> classes = sessionManagerB34.getAnnotatedClasses();

        assertThat( classes.size(), is( 2 ) );
        assertThat( classes, hasItems( "org.test.Class1", "org.test.Class3" ) );
    }

    @Test
    public void sessionManagerHasCorrectExecutionInterceptors()
    {
        final List<String> classes = sessionManagerB34.getExecutionInterceptorClasses();

        assertThat( classes.size(), is( 2 ) );
        assertThat( classes, hasItems( "org.test.Class4", "org.test.Class5" ) );
    }

    @Test
    public void sessionManagerHasCorrectBapiInterceptors()
    {
        final List<String> classes = sessionManagerB34.getBapiInterceptorClasses();

        assertThat( classes.size(), is( 2 ) );
        assertThat( classes, hasItems( "org.test.Class6", "org.test.Class7" ) );
    }

    @Test
    public void sessionManagerHasCorrectValidationMode()
    {
        final ValidationMode validationMode = sessionManagerB34.getValidationMode();
        assertThat( validationMode, is( ValidationMode.CALLBACK ) );
    }

    @Test
    public void simpleHibersapXmlWithANamespaceCanBeBuilt()
    {
        final HibersapJaxbXmlParser hiberSapJaxbXmlParser = new HibersapJaxbXmlParser();
        HibersapConfig config = hiberSapJaxbXmlParser.parseResource( "/xml-configurations/hibersapSample.xml" );
        SessionManagerConfig sessionManagerNSP = config.getSessionManager( "NSP" );

        assertThat( sessionManagerNSP, is( notNullValue() ) );
        assertThat( sessionManagerNSP.getJcaConnectionFactory(), equalTo( "java:jboss/eis/sap/NSP" ) );
    }
}
