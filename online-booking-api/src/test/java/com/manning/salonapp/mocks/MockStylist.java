package com.manning.salonapp.mocks;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import skyglass.salonapp.salonservice.SalonServiceDetail;


public class MockStylist {
    String name;
    List<SalonServiceDetail> services;
    List<Integer> slots;


    public Set<SalonServiceDetail> servicesAsSet(){
        return new HashSet<SalonServiceDetail>(services);
    }
}
