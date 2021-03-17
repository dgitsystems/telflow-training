/*
 * Copyright (c) 2010-2017 DGIT Consultants Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT Consultants Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.csv.address.transform;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telflow.csv.address.cimbuilder.NZAddressCimBuilder;
import com.telflow.csv.address.cimbuilder.NZFromLocationMgrFormat;
import com.telflow.csv.address.model.OnNetAddressModel;

import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;

/**
 * Extracts and transforms mysql location data from CSV dump into json to be updated in ES
 * 
 * @author Muhammad Rukunuzzaman
 */
public class ESLocationImportHelper {

    static final transient Logger LOG = LoggerFactory.getLogger(ESLocationImportHelper.class);

    private NZAddressCimBuilder<OnNetAddressModel> auAddressCimBuilder;

    /**
     * @param model OnNetAddressModel
     * @return Address in AustralianAddressFormat
     * @throws ParseException ParseException
     * @throws JsonProcessingException JsonProcessingException
     */
    public NewZealandAddress processAuAddress(OnNetAddressModel model) throws ParseException, JsonProcessingException {

        auAddressCimBuilder = new NZFromLocationMgrFormat();
        // prepare {@code AustralianAddress} object from Object data
        NewZealandAddress address = auAddressCimBuilder.buildCimFromCimFields(model);
        
        return address;
    }

}
