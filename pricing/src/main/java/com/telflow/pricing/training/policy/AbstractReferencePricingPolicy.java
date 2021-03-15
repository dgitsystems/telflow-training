package com.telflow.pricing.training.policy;

import biz.dgit.schemas.telflow.cim.v3.Charge;
import biz.dgit.schemas.telflow.cim.v3.Rate;
import biz.dgit.schemas.telflow.cim.v3.RateCode;
import biz.dgit.schemas.telflow.cim.v3.RateItem;
import biz.dgit.schemas.telflow.cim.v3.RateItems;
import com.telflow.fabric.businessservice.pricing.interfaces.PricingPolicy;
import com.telflow.fabric.businessservice.pricing.interfaces.PricingPolicyResponse;
import com.telflow.fabric.businessservice.pricing.interfaces.RequestContext;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to create Custom Pricing Policy
 * 
 * @author Sandeep Vasani
 *
 */
public abstract class AbstractReferencePricingPolicy implements PricingPolicy {
    /**
     * Logger
     */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    private final String id;

    public AbstractReferencePricingPolicy(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public PricingPolicyResponse process(RequestContext request, List<Pair<Rate, RateItem>> rates) {
        PricingPolicyResponse response = new PricingPolicyResponse();
        this.LOG.trace("Running '{}' Custom Policy", getId());
        rates.parallelStream()
            .peek(x -> this.LOG.trace("Processing RateCode ID: {}", x.getValue().getRateCode().getID()))
            .forEach(x -> newCharge(response, request.getRequest().getRateItems(), x.getValue(), x.getKey()));
        return response;
    }
    
    protected abstract void newCharge(PricingPolicyResponse response, RateItems requestRateItems,
            RateItem item, Rate rate);
    
    /**
     * Finds {@code RateItem} by RateCode ID from List of RateItems
     * 
     * @param rateItems
     *            {@code List<RateItem>}
     * @param RateCodeItemName
     *            RateCode ID
     * @return {@code RateItem}
     */
    protected RateItem getRateItem(List<RateItem> rateItems, String RateCodeItemName) {
        return rateItems.stream()
                .peek(x -> this.LOG.trace("Request RateCodeItem ID: {}", x.getRateCode().getID()))
                .filter(x -> RateCodeItemName.equals(x.getRateCode().getID()))
                .findFirst().orElse(null);
    }

    /**
     * Creates a Charge Item to be added to GetPricingResponse.
     * 
     * @param priceValue The calculated value of the new charge item.
     * @param taxValue The amount of tax for the current charge
     * @param rateCode {@code RateCode}
     * @return {@code Charge}
     */
    protected Charge createChargeItem(BigDecimal priceValue, BigDecimal taxValue, RateCode rateCode) {
        return new Charge()
                .withPrice(priceValue)
                .withTax(taxValue)
                .withRateCodeID(rateCode.getID())
                .withName(rateCode.getName())
                .withDescription(rateCode.getDescription())
                .withChargeType(rateCode.getChargeType())
                .withRecurringChargePeriod(rateCode.getRecurringChargePeriod());
    }

}

