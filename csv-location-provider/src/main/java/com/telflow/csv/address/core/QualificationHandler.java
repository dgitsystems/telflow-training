package com.telflow.csv.address.core;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionItems;
import biz.dgit.schemas.telflow.cim.v3.Fault;
import biz.dgit.schemas.telflow.cim.v3.GeographicAddress;
import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;
import biz.dgit.schemas.telflow.cim.v3.PerformServiceQualificationRequest;
import biz.dgit.schemas.telflow.cim.v3.PerformServiceQualificationResponse;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRole;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRoles;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualification;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationItem;
import biz.dgit.schemas.telflow.cim.v3.ServiceQualificationServiceQualificationResult;

import com.telflow.cim.converter.CimConverter;
import com.telflow.factory.authentication.AuthClient;
import com.telflow.factory.authentication.exception.AuthenticationException;
import com.telflow.factory.common.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

/**
 * Handle received PerformServiceQualification requests
 */
public final class QualificationHandler {

    private static final transient Logger LOG = LoggerFactory.getLogger(QualificationHandler.class);

    private static QualificationClient qualificationClient;

    private static CimConverter converter;

    private QualificationHandler() {
        
    }

    /**
     * Init the QualificationHandler
     * @param authClient the authClient
     * @param cimConverter the cim converter
     */
    public static void init(QualificationClient authClient, CimConverter cimConverter) {
        converter = cimConverter;
        qualificationClient = new QualificationClient();
    }

    /**
     * handle nbn service qualification request. Called from restlet
     * @param availabilityRequest the PerformServiceQualificationRequest
     * @return the response
     */
    @SuppressWarnings("checkstyle:methodlength")
    public static String handleQualificationRequest(String availabilityRequest) {
        try {
            LOG.info("Received availability request {}", availabilityRequest);
            PerformServiceQualificationRequest request =
                    converter.unmarshal(availabilityRequest, PerformServiceQualificationRequest.class);
            PlaceInteractionRole role = getNBNPlaceInteractionRole(request);
            if (role == null) {
                LOG.info("No place with address name {} found, returning empty response", "Training");
                return createEmptyResponse();
            }
            String addressId = role.getPlace().getID();
            String formattedAddress = ((NewZealandAddress) role.getPlace()).getFormattedAddress();
            RequestContext rc = new RequestContext();
            rc.setId(addressId);
            
            PerformServiceQualificationResponse performServiceQualificationResponse = getQualification(rc, role);
            
            String response = converter.marshal(performServiceQualificationResponse);
            LOG.trace("Received response{}", response);
            
            return response;
        } catch (WebApplicationException webex) {
            LOG.error("Failed to contact Address Search API", webex);
            String responseBody = webex.getResponse().readEntity(String.class);
            LOG.error("Response was: ", responseBody);
            return handleError(Integer.toString(webex.getResponse().getStatus()), responseBody);
        } catch (AuthenticationException ex) {
            LOG.error("Could not get auth token when attempting get wideband feasibility", ex);
            return handleError(ex.getCode(), ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Could not complete Service Qualification request: {}, exception {}", availabilityRequest,
                e.getMessage());
            return handleError("500", e.getMessage());
        }
    }

    private static PlaceInteractionRole getNBNPlaceInteractionRole(PerformServiceQualificationRequest request) {
        return request.getServiceQualification().getBusinessInteractionLocations()
            .getPlaceInteractionRole().stream()
            .findFirst()
            .orElse(null);
    }

    private static String handleError(String status, String response) {
        PerformServiceQualificationResponse qualResponse = new PerformServiceQualificationResponse()
            .withException(new Fault().withCode(status).withPublicMessage(response));
        return converter.marshal(qualResponse);
    }

    private static String createEmptyResponse() {
        PerformServiceQualificationResponse qualResponse = new PerformServiceQualificationResponse();
        return converter.marshal(qualResponse);
    }
    
    private static PerformServiceQualificationResponse getQualification(RequestContext rc, PlaceInteractionRole role) {
        
        PerformServiceQualificationResponse response = new PerformServiceQualificationResponse();
        
        PlaceInteractionRoles rolez = new PlaceInteractionRoles().withPlaceInteractionRole(role);
        
        ServiceQualificationItem item = new ServiceQualificationItem().withItemInvolvesLocations(rolez)
                .withQualificationResult(ServiceQualificationServiceQualificationResult.SERVICEABLE);
        
        BusinessInteractionItems items = new BusinessInteractionItems().withBusinessInteractionItem(item);
        
        ServiceQualification qual = new ServiceQualification().withBusinessInteractionComprisedOf(items);
        
        response.withServiceQualification(qual);
        
        return response;
    }

}
