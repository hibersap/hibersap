package org.hibersap.examples.model;

import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;

@BapiStructure
public class Customer
{
    @Parameter("CUSTOMERID")
    private String number;

    @Parameter("CUSTNAME")
    private String name;

    @Parameter("FORM")
    private String formOfAddress;

    @Parameter("COUNTR_ISO")
    private String country;

    @Parameter("CITY")
    private String city;

    @Parameter("POSTCODE")
    private String postalCode;

    @Parameter("STREET")
    private String street;

    @Parameter("PHONE")
    private String telephoneNumber;

    @Parameter("EMAIL")
    private String email;

    public String getFormOfAddress()
    {
        return formOfAddress;
    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public String getCountry()
    {
        return country;
    }

    public String getCity()
    {
        return city;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public String getStreet()
    {
        return street;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public String getEmail()
    {
        return email;
    }
}
