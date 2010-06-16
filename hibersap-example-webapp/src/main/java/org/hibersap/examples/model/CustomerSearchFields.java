package org.hibersap.examples.model;

import java.io.Serializable;

public class CustomerSearchFields implements Serializable
{
    private String customerNamePattern;
    private Integer maxRows;

    public CustomerSearchFields(String customerNamePattern, Integer maxRows)
    {
        this.customerNamePattern = customerNamePattern;
        this.maxRows = maxRows;
    }

    public String getCustomerNamePattern()
    {
        return customerNamePattern;
    }

    public void setCustomerNamePattern(String customerNamePattern)
    {
        this.customerNamePattern = customerNamePattern;
    }

    public Integer getMaxRows()
    {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows)
    {
        this.maxRows = maxRows;
    }
}