package com.telflow.pricing.training.policy;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.telflow.fabric.businessservice.pricing.interfaces.PricingPolicyResponse;

import biz.dgit.schemas.telflow.cim.v3.Fault;
import biz.dgit.schemas.telflow.cim.v3.Rate;
import biz.dgit.schemas.telflow.cim.v3.RateCode;
import biz.dgit.schemas.telflow.cim.v3.RateItem;
import biz.dgit.schemas.telflow.cim.v3.RateItems;

/**
 * Multiply Device type and quantity
 * @author ymadiana
 *
 */
public class DeviceQuantityPolicy extends AbstractReferencePricingPolicy {
    
    final static String QUANTITY_CUSTOM = "MAXIS-QUANTITY-CUSTOM";

    final static String DEVICE_CUSTOM_HM20 = "MAXIS-DEVICE-CUSTOM-HM20";
    
    final static String DEVICE_CUSTOM_HM20PRO = "MAXIS-DEVICE-CUSTOM-HM20PRO";
    
    final static String DEVICE_CUSTOM_SNOTE9 = "MAXIS-DEVICE-CUSTOM-SNOTE9";
    
    final static String DEVICE_CUSTOM_STAB8 = "MAXIS-DEVICE-CUSTOM-STAB8";

    public DeviceQuantityPolicy(String id) {
        super(id);
    }

    @Override
    protected void newCharge(PricingPolicyResponse response, RateItems requestRateItems, RateItem item, Rate rate) {
        if (requestRateItems == null) {
            return;
        }
        
        RateItem quantityRate = getRateItem(requestRateItems.getRateItem(), QUANTITY_CUSTOM);
        
        if (quantityRate == null || StringUtils.isEmpty(quantityRate.getValue())) {
            return;
        }
        
        String quantity = quantityRate.getValue();
        String namae = rate.getRateCode().getDescription();
        String deviceDesc = String.format("%s (qty. %s)", namae, quantity);
        
        try {
            BigDecimal quantities = new BigDecimal(quantity);
            BigDecimal deviceQPrice = rate.getPrice();
            BigDecimal deviceQTax = rate.getTax() == null ? BigDecimal.ZERO : rate.getTax();

            BigDecimal priceValue =  quantities.multiply(deviceQPrice);
            BigDecimal taxValue = quantities.multiply(deviceQTax);
            this.LOG.debug("Device Price: '{}', Tax: '{}'", priceValue, taxValue);

            RateCode newRate = rate.getRateCode();
            newRate.setName(deviceDesc);
            
            response.getCharges().add(createChargeItem(priceValue, taxValue, newRate));
        } catch (NumberFormatException ex) {
            response.faults.add(new Fault().withCode(this.getId())
                    .withPublicMessage(String.format("Quantity should be a numeric and not '%s'", quantity)));
        }
    }

}
