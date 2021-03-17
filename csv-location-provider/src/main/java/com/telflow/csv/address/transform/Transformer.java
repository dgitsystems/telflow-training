package com.telflow.csv.address.transform;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telflow.csv.address.cimbuilder.NZAddressCimBuilderUtil;
import com.telflow.csv.address.model.OnNetAddressModel;

import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;

/**
 * Class to convert List of String from CSV to Telflow Address format
 *
 */
public class Transformer {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(Transformer.class);
    
    /**
     * @param strAddresses List of Addresses from CSV File
     * @return telflowAddresses List of Addresses in Telflow Format
     * @throws JsonProcessingException JsonProcessingException
     * @throws ParseException ParseException
     */
    public List<NewZealandAddress> buildTelflowAddressJsonFormat(List<String> strAddresses) 
            throws JsonProcessingException, ParseException {
        
        List<NewZealandAddress> telflowAddresses = new ArrayList<NewZealandAddress>();
        
        for (String strAddress : strAddresses) {
            if (StringUtils.isEmpty(strAddress)) {
                continue;
            }
            OnNetAddressModel model = convertStringIntoOnNet(strAddress);
            
            if (!isAddressValid(model)) {
                continue;
            }
            
            ESLocationImportHelper helper = new ESLocationImportHelper();
            NewZealandAddress telflowAddress = helper.processAuAddress(model);
            
            telflowAddresses.add(telflowAddress);
        }
        return telflowAddresses;
    }
    
    private OnNetAddressModel convertStringIntoOnNet(String csv) {
        String[] line = csv.split(",", -1);
        
        OnNetAddressModel model = new OnNetAddressModel();
        model.setSiteCode(line[NZAddressCimBuilderUtil.Indices.ZERO.ordinal()]);
        model.setName(line[NZAddressCimBuilderUtil.Indices.ONE.ordinal()]);
        model.setRoadNumber1(line[NZAddressCimBuilderUtil.Indices.TWO.ordinal()]);
        model.setRoadName(line[NZAddressCimBuilderUtil.Indices.THREE.ordinal()]);
        model.setRoadTypeCode(line[NZAddressCimBuilderUtil.Indices.FOUR.ordinal()]);
        model.setCityOrTown(line[NZAddressCimBuilderUtil.Indices.FIVE.ordinal()]);
        model.setCountry(line[NZAddressCimBuilderUtil.Indices.SIX.ordinal()]);
        model.setPostcode(line[NZAddressCimBuilderUtil.Indices.SEVEN.ordinal()]);
        model.setParcelID(line[NZAddressCimBuilderUtil.Indices.EIGHT.ordinal()]);
        model.setLocalityName(line[NZAddressCimBuilderUtil.Indices.NINE.ordinal()]);
        
        return model;
    }

    private boolean isAddressValid(OnNetAddressModel model) {
        if (StringUtils.isEmpty(model.getSiteCode())) {
            LOG.warn("Address with Site Code : {} is not valid ! Address must contain "
                    + "following fields : Site Code", model.getSiteCode() );
            return false;
        }
        
        return true;
    }

}
