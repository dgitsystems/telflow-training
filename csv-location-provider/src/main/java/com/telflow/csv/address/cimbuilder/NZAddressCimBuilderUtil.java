/*
 * Copyright (c) 2010-2017 DGIT Consultants Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT Consultants Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.csv.address.cimbuilder;

import biz.dgit.schemas.telflow.cim.v3.ServiceQualification;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationServiceQualificationResult;
import com.telflow.cim.util.CimPath;
import com.telflow.cim.util.exception.CharacteristicNotSpecifiedException;
import com.telflow.cim.util.exception.InvalidCimPathException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class to help building {@code AustralianAddress} object
 * 
 * @author Muhammad Rukunuzzaman
 */
public final class NZAddressCimBuilderUtil {

    private static final Logger LOG = LoggerFactory.getLogger(NZAddressCimBuilderUtil.class);

    private NZAddressCimBuilderUtil() {
    }

    /**
     * Enriches a {@code ServiceQualification} object from passed qualification fields
     * 
     * @param sq the {@code ServiceQualification} object
     * @param qualChars a {@code Map} containing qualification characteristics id names 
     * @return the enriched {@code ServiceQualification} object
     */
    public static ServiceQualification enrichQualChars(ServiceQualification sq,
        final Map<String, String> qualChars) {
        
        CimPath cimPath = new CimPath(sq);

        for (Map.Entry<String, String> qualChar : qualChars.entrySet()) {
            String name = qualChar.getKey();
            
            String path = "/" + name;
            String value = qualChar.getValue();

            if (value == null || value.isEmpty()) {
                continue;
            }
            
            if (StringUtils.equals("Status", name)) {
                sq.setQualificationResult(getQualResult(value));
            }

            try {
                cimPath.set(path, value, name);
            } catch (InvalidCimPathException | CharacteristicNotSpecifiedException e) {
                LOG.debug("Error setting Qual DescribedBy on characteristic '{}', name: '{}', value:'{}'", path, name,
                    value);
            }
        }
        
        return sq;
    }
    
    /**
     * Calculate the serviceability status from a text string.
     * @param resultString string to process
     * @return the Service Qualification Result
     */
    public static ServiceQualificationServiceQualificationResult getQualResult(String resultString) {
        if ("Building Lit".equalsIgnoreCase(resultString)) {
            return ServiceQualificationServiceQualificationResult.SERVICEABLE;
        }
        return ServiceQualificationServiceQualificationResult.UNSERVICEABLE;
    }

    /**
     * returns an empty string if null
     * 
     * @param text the string to check null
     * @return an empty string if null
     */
    public static String n2e(String text) {
        return text == null ? "" : text;
    }

    /**
     * returns null if the string is empty
     * 
     * @param text the string to check null
     * @return an empty string if null
     */
    public static String e2n(String text) {
        return StringUtils.isEmpty(text) ? null : text;
    }

    /**
     * Field indices
     */
    @SuppressWarnings("checkstyle:javadocvariable")
    public enum Indices {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        ELEVEN,
        TWELVE,
        THIRTEEN,
        FOURTEEN,
        FIFTEEN,
        SIXTEEN,
        SEVENTEEN,
        EIGHTEEN,
        NINETEEN,
        TWENTY,
        TWENTYONE,
        TWENTYTWO,
        TWENTYTHREE,
        TWENTYFOUR,
        TWENTYFIVE,
        TWENTYSIX,
        TWENTYSEVEN,
        TWENTYEIGHT,
        TWENTYNINE;
    }

}
