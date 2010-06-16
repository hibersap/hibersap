package org.hibersap.examples.dao;

import org.hibersap.examples.model.CustomerInfo;
import org.hibersap.examples.model.CustomerSearchFields;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to flight customers in SAP.
 */
public class SapCustomerDao implements CustomerDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SapCustomerDao.class);

    // The Hibersap session manager
    private final SessionManager sessionManager;

    /**
     * @param sessionManager The Hibersap session manager
     */
    public SapCustomerDao(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    /**
     * {@inheritDoc}
     */
    public CustomerInfo findCustomerInfo(CustomerSearchFields fields)
    {
        LOGGER.debug("Looking for customer data in SAP...");
        CustomerInfo bapi = new CustomerInfo(fields.getMaxRows(), fields.getCustomerNamePattern());
        final Session session = sessionManager.openSession();
        try
        {
            bapi.setNamePattern(fields.getCustomerNamePattern());
            bapi.setMaxRows(fields.getMaxRows());
            session.execute(bapi);
            LOGGER.debug(String.format("%d customer(s) read from SAP", bapi.getCustomers().size()));
        }
        finally
        {
            session.close();
        }

        return bapi;
    }
}
