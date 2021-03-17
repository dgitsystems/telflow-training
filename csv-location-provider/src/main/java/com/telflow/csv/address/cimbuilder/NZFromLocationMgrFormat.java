/*
 * Copyright (c) 2010-2017 DGIT Consultants Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT Consultants Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.csv.address.cimbuilder;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.telflow.csv.address.model.OnNetAddressModel;

import biz.dgit.schemas.telflow.cim.v3.GeoPoint;
import biz.dgit.schemas.telflow.cim.v3.GeographicLocation;
import biz.dgit.schemas.telflow.cim.v3.GeographicLocations;
import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualification;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationServiceQualificationType;

/**
 * A {@code AustralianAddress} object builder from location manager formatted input data
 * <p>
 * Sample location manger data:<br>
 * <br>
 * Index FileldName SampleValue<br>
 * <br>
 * 2 UNIQUEID STCK <br>
 * 0 FORMATTED ADDRESS 20 Bridge Street, Sydney NSW 2000 <br>
 * 1 PROPOSED RELEASE Q4-CY19 (Dec19) <br>
 * 3 CLASS Data Centre
 */
public class NZFromLocationMgrFormat implements NZAddressCimBuilder<OnNetAddressModel> {
    
    @Override
    public NewZealandAddress buildCimFromCimFields(final OnNetAddressModel fields) {
        
        NewZealandAddress auAddress = new NewZealandAddress();
        auAddress.setID(NZAddressCimBuilderUtil.e2n(
                fields.getSiteCode()));
        
        auAddress.withFormattedAddress(NZAddressCimBuilderUtil.e2n(
                fields.getFormattedAddress())).withPostcode(fields.getPostcode())
            .withRoadName(fields.getRoadName()).withRoadTypeCode(fields.getRoadTypeCode())
            .withLocalityName(fields.getLocalityName())
            .withRoadNumber1(fields.getRoadNumber1())
            .withCountry(fields.getCountry());
         
        return auAddress;
    }

    @Override
    public ServiceQualification getSiteQualification(final OnNetAddressModel qualFields) throws ParseException {
        ServiceQualification sq = new ServiceQualification()
            .withQualificationType(ServiceQualificationServiceQualificationType.SITE_QUALIFICATION);
        
        Map<String, String> qualChars = new HashMap<String, String>();
        qualChars.put("Status", qualFields.getStatus());
        
        return NZAddressCimBuilderUtil.enrichQualChars(sq, qualChars);
    }
}
