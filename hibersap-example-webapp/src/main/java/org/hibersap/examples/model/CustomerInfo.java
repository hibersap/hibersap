package org.hibersap.examples.model;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.bapi.BapiRet2;

import java.util.ArrayList;
import java.util.List;

@Bapi("BAPI_FLCUST_GETLIST")
public class CustomerInfo
{
    @Import
    @Parameter("MAX_ROWS")
    private int maxRows;

    @Import
    @Parameter("CUSTOMER_NAME")
    private String namePattern;

    @Table
    @Parameter("RETURN")
    private List<BapiRet2> returnMessages = new ArrayList<BapiRet2>();

    @Table
    @Parameter("CUSTOMER_LIST")
    private List<Customer> customers = new ArrayList<Customer>();

    public CustomerInfo(int maxRows, String namePattern)
    {
        this.maxRows = maxRows;
        this.namePattern = namePattern;
    }

    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows;
    }

    public void setNamePattern(String namePattern)
    {
        this.namePattern = namePattern;
    }

    public List<Customer> getCustomers()
    {
        return customers;
    }

    public List<BapiRet2> getReturnMessages()
    {
        return returnMessages;
    }
}
