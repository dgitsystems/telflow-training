/*
 * Copyright (c) 2010-2017 DGIT Consultants Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT Consultants Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.csv.address.cimbuilder;

import java.text.ParseException;

import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualification;

/**
 * A simple interface to create a CIM object from a array of CIM fields. An example could be a CSV file where each row
 * contains CIM fileds in columns
 * 
 *  @param <E>
 * 
 * @author Muhammad Rukunuzzaman
 *
 */
public interface NZAddressCimBuilder<E> {

    /**
     * Builds {@code AustralianAddress} object from CIM fields
     * 
     * @param fields the CIM fields
     * @return the generated CIM object
     */
    NewZealandAddress buildCimFromCimFields(E fields);

    /**
     * Get Site Qualification
     * @param qualFields the CIM fields
     * @return Site Qualification
     * @throws ParseException any exception during qualification generation
     */
    ServiceQualification getSiteQualification(E qualFields) throws ParseException;
}
