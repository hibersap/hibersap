package org.hibersap.examples.dao;

import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.model.Customer;
import org.hibersap.examples.model.CustomerInfo;
import org.hibersap.examples.model.CustomerSearchFields;
import org.hibersap.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class SapCustomerDaoTest
{
    private static Logger LOG = LoggerFactory.getLogger(SapCustomerDaoTest.class);

    private SapCustomerDao dao;

    @Before
    public void setup() throws IOException
    {
        setLibPathSystemProperty();
        LOG.info("java.library.path=" + System.getProperty("java.library.path"));
        final AnnotationConfiguration config = new AnnotationConfiguration("A12");
        config.getSessionManagerConfig().addAnnotatedClass(CustomerInfo.class);
        dao = new SapCustomerDao(config.buildSessionManager());
    }

    @Test
    public void testGetCustomerData() throws Exception
    {
        final SessionManager sessionManager = new AnnotationConfiguration().buildSessionManager();
        System.out.println("SMConfig: " + sessionManager.getConfig());

        final CustomerSearchFields searchFields = new CustomerSearchFields("*", 1);

        final List<Customer> list = dao.findCustomerInfo(searchFields).getCustomers();

        assertEquals("The flight application in SAP doesn't seem to be initialized. Run program SAPBC_DATA_GENERATOR in transaction SE38", 1, list.size());
    }

    /**
     * Needed to locally run JCo tests in the IDE
     *
     * @throws IOException If properties file can't be found on the classpath
     */
    private void setLibPathSystemProperty()
            throws IOException
    {
        final Properties properties = new Properties();
        properties.load(SapCustomerDaoTest.class.getResourceAsStream("/libpath.properties"));
        final String libPath = properties.getProperty("jco.native.lib.path");
        System.setProperty("java.library.path", libPath == null ? "" : libPath);
    }
}
