package com.telflow.process.training.listeners;

import org.activiti.engine.delegate.DelegateExecution;

import com.telflow.cim.util.CimPath;
import com.telflow.cim.util.exception.CharacteristicNotSpecifiedException;
import com.telflow.cim.util.exception.InvalidCimPathException;
import com.telflow.process.listeners.TelflowProcessListener;
import com.telflow.process.models.v2.BusinessInteractionModel;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteraction;

/**
 * @author macbookpro2
 *
 */
public class YohanJavaListener extends TelflowProcessListener {

    @Override
    public void doExecute(DelegateExecution execution) throws Exception {
        String id = execution.getVariable("businessInteractionId", String.class);
        
        BusinessInteractionModel biModel = new BusinessInteractionModel(id);
        
        // do something with BI
        updateBiCharacteristics(biModel.get());
        
        // update BI
        biModel.update();
        
        
        execution.setVariable("yohanPV", "Yohan Custom Value");
    }
    
    private void updateBiCharacteristics(BusinessInteraction bi) 
            throws CharacteristicNotSpecifiedException, InvalidCimPathException {
        bi.setName("Something");
        CimPath cimPath = new CimPath(bi);
        cimPath.set("/customerName", "Yohan123");
    }

}
