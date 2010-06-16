package org.hibersap.examples.dao;

import org.hibersap.examples.model.CustomerSearchFields;
import org.hibersap.examples.model.CustomerInfo;

/**
 * Provides access to flight customers.
 */
public interface CustomerDao
{
    /**
     * Gets information on customers according to the given search fields.
     *
     * @param fields The search fields.
     * @return A CustomerInfo object.
     */
    CustomerInfo findCustomerInfo(CustomerSearchFields fields);
}
