package com.telflow.csv.address.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.cim.converter.CimConverter;
import com.telflow.factory.authentication.exception.AuthenticationException;
import com.telflow.factory.common.RequestContext;

import biz.dgit.schemas.telflow.cim.v3.AddressSearchRequest;
import biz.dgit.schemas.telflow.cim.v3.AddressSearchResponse;
import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionItem;
import biz.dgit.schemas.telflow.cim.v3.Fault;
import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;

/**
 * Handle received AddressSearch requests
 */
public final class AddressSearchHandler {

    private static final transient Logger LOG = LoggerFactory.getLogger(AddressSearchHandler.class);
    
    private static final String UNIT_NUMBER = "unitNumber";
    
    private static final String LEVEL_NUMBER = "levelNumber";
    
    private static final String ADDRESS_SITE_NAME = "addressSiteName";

    private static AddressSearchClient addressSearchClient;

    private static CimConverter converter;

    private AddressSearchHandler() {
        
    }

    /**
     * Init the AddressSearchHandler
     * @param authClient the authClient
     * @param cimConverter the cim converter
     */
    public static void init(List<NewZealandAddress> addresses, CimConverter cimConverter) {
        converter = cimConverter;
        addressSearchClient = new AddressSearchClient(addresses);
    }

    /**
     * handle an Address Search request. Called from restlet
     * @param addressSearchRequest the AddressSearchRequest
     * @return the response
     */
    public static String handleAddressSearchRequest(String addressSearchRequest) {
        try {
            RequestContext rc = new RequestContext();
            rc.setContextProperties(extractRequestParameters(addressSearchRequest));
            rc.setCurrentMessage(addressSearchRequest);
            
            AddressSearchResponse addressSearchResponse = addressSearchClient.searchAddress(rc);
            String response = converter.marshal(addressSearchResponse);
            
            LOG.trace("NBN response: {}", response);
            return response;
        } catch (WebApplicationException webex) {
            LOG.error("Failed to contact NBN Address Search API", webex);

            String responseBody = webex.getResponse().readEntity(String.class);
            LOG.error("Response was: ", responseBody);
            return handleError(Integer.toString(webex.getResponse().getStatus()),
                responseBody);
        } catch (AuthenticationException ex) {
            LOG.error("Could not get auth token when attempting get address", ex);
            return handleError(ex.getCode(), ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Could not complete address search request: {}, exception {}", addressSearchRequest,
                e.getMessage());
            return handleError("500", e.getMessage());
        }
    }
    
    private static Map<String, String> extractRequestParameters(String addressSearchRequest) throws Exception {
        Map<String, String> requestParameters = new HashMap<String, String>();
        AddressSearchRequest request = converter.unmarshal(addressSearchRequest, AddressSearchRequest.class);
        
        BusinessInteractionItem item = request.getAddressSearch().getBusinessInteractionComprisedOf()
                .getBusinessInteractionItem().get(0);
        
        NewZealandAddress place = (NewZealandAddress) item.getItemInvolvesLocations().getPlaceInteractionRole()
                .get(0).getPlace();
        
        requestParameters.put("formattedAddress", place.getFormattedAddress());
        
        requestParameters.put("roadNumber1", place.getRoadNumber1());
        requestParameters.put("roadName", place.getRoadName());
        requestParameters.put("localityName", place.getLocalityName() != null ? place.getLocalityName() : "");
        return requestParameters;
    }
    
    private static String handleError(String status, String response) {
        AddressSearchResponse addressSearchResponse = new AddressSearchResponse()
            .withException(new Fault().withCode(status).withPublicMessage(response));
        return converter.marshal(addressSearchResponse);
    }

}
