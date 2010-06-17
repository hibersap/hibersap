package org.hibersap.examples.ui;

import org.apache.wicket.protocol.http.WebApplication;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.dao.CustomerDao;
import org.hibersap.examples.dao.SapCustomerDao;
import org.hibersap.examples.model.CustomerInfo;
import org.hibersap.session.SessionManager;

/**
 * Initializes the Wicket application.
 */
public class WicketApplication extends WebApplication
{
    private CustomerDao customerDao;

    @Override
    protected void init()
    {
        super.init();
        this.getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        // Have to do this since the maven-jetty-plugin config doesn't overwrite existing environment variables
        System.setProperty("java.library.path", System.getProperty("jco.native.lib.path"));

        // Build the Hibersap session manager and DAO
        final AnnotationConfiguration config = new AnnotationConfiguration("A12");
        config.getSessionManagerConfig().addAnnotatedClass(CustomerInfo.class);
        final SessionManager sessionManager = config.buildSessionManager();
        customerDao = new SapCustomerDao(sessionManager);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    public Class<SearchCustomersPage> getHomePage()
    {
        return SearchCustomersPage.class;
    }

    public CustomerDao getCustomerDao()
    {
        return customerDao;
    }

    public static WicketApplication get()
    {
        return (WicketApplication) WebApplication.get();
    }
}
