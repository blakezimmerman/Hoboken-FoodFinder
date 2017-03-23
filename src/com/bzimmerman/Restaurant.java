package com.bzimmerman;

/**
 * Created by bzimmerman on 7/19/15.
 */

public class Restaurant
{
    private String name;
    private String address;
    private boolean duckbills;

    public Restaurant (String n, String a, boolean d)
    {
        name = n;
        address = a;
        duckbills = d;
    }

    public String getName()
    { return name; }

    public String getAddress()
    { return address; }

    public boolean getDuckbills()
    { return duckbills; }
}