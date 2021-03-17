package com.telflow.csv.address.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.factory.common.RequestContext;

import biz.dgit.schemas.telflow.cim.v3.AddressMatchItem;
import biz.dgit.schemas.telflow.cim.v3.AddressSearch;
import biz.dgit.schemas.telflow.cim.v3.AddressSearchResponse;
import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionItems;
import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRole;
import biz.dgit.schemas.telflow.cim.v3.PlaceInteractionRoles;

public class AddressSearchClient {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(AddressSearchClient.class);
    
    private static final String FORMATTED_ADDRESS = "formattedAddress";
    
    private List<NewZealandAddress> addresses;
    
    public AddressSearchClient(List<NewZealandAddress> addresses) {
        this.addresses = addresses;
    }

    public AddressSearchResponse searchAddress(RequestContext rc) {
        Map<String, String> requestParam = rc.getContextProperties();
        
        String formattedAddress = requestParam.get(FORMATTED_ADDRESS);
        
        LOG.info("SEARCHING ADDRESS : {}", formattedAddress);
        
        for (NewZealandAddress address : this.addresses) {
            if (address.getFormattedAddress().contains(formattedAddress)) {
                return getAddress(address);
            }
        }
        
        return null;
    }
    
    private AddressSearchResponse getAddress(NewZealandAddress address) {
        AddressSearchResponse response = new AddressSearchResponse();
        
        PlaceInteractionRole place = new PlaceInteractionRole().withPlace(address);
        AddressMatchItem items = new AddressMatchItem().withItemInvolvesLocations(new PlaceInteractionRoles());
        items.getItemInvolvesLocations().getPlaceInteractionRole().add(place);
        
        AddressSearch addressSearch = new AddressSearch()
                .withName("Training")
                .withBusinessInteractionComprisedOf(
                        new BusinessInteractionItems().withBusinessInteractionItem(items));
        
        response.withAddressSearch(addressSearch);
        return response;
    }
    
}
