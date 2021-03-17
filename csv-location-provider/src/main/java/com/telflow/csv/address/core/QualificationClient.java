package com.telflow.csv.address.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.factory.common.RequestContext;
import com.telflow.factory.common.exception.ClientTransactionException;
import com.telflow.factory.common.exception.ServerTransactionException;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionItems;
import biz.dgit.schemas.telflow.cim.v3.PerformServiceQualificationResponse;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRole;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRoles;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualification;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationItem;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationServiceQualificationResult;

/**
 * Client for reaching NBN qual endpoints
 */
public final class QualificationClient {

    private static final transient Logger LOG = LoggerFactory.getLogger(QualificationClient.class);


    /**
     * Send a qual feasibility request
     * @param rc The request context to process.
     * @return the resource as a string
     * @throws ClientTransactionException When a HTTP 4xx exception is returned from the endpoint.
     * @throws ServerTransactionException When a HTTP 5xx exception is returned from the endpoint.
     */
    public PerformServiceQualificationResponse getQualification(RequestContext rc, PlaceInteractionRole role) {
        
        PerformServiceQualificationResponse response = new PerformServiceQualificationResponse();
        
        
        return response;
    }

}
