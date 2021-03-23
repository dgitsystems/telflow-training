package com.telflow.process.training.listeners;

import com.telflow.process.listeners.TelflowProcessListener;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.commons.lang3.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Processes System Bus Event response code using code interpretation mapping
 * and set following process variables:
 * * receivedCode: Received response code
 * * receivedCodeDisplayName: Display name for received response code
 * * receivedCodeLoggingRequired: To log the response code as internal note or not
 * * receivedCodeType: Type of response code from - Success, Failure, Update, Process, Internal, Interest
 * * receivedCodeSubProcessId: Subprocess Id for the received response code,
 *                             'NoProcess' is set if no subprocess needs to launched
 * * successFlag: Set true if code is not failure or if code not found in interpretation mapping
 * 
 * @author Sandeep Vasani
 */
public class ProcessEvent extends TelflowProcessListener {

    private static final String RESPONSE_CODE = "responseCode";

    private static final String RECEIVED_CODE = "receivedCode";

    private static final String SUCCESS_FLAG = "successFlag";

    private static final String EXTERNAL_ID = "externalId";

    private Expression category;

    private Expression codeMappingPv;

    public void setCategory(Expression category) {
        this.category = category;
    }

    public void setCodeMappingPv(Expression codeMappingPv) {
        this.codeMappingPv = codeMappingPv;
    }

    @Override
    public void doExecute(DelegateExecution execution) throws Exception {
        final String reqCategory = ProcessListenerHelper.getRequiredExpressionValue(
                execution, this.category, String.class);
        String codeMappingPvName = ProcessListenerHelper.getExpressionValue(
                execution, this.codeMappingPv, String.class);
        if (StringUtils.isEmpty(codeMappingPvName)) {
            codeMappingPvName = "shared.iwfpeoResponseCodeBlob";
        }
        JSONArray codeMappings = (JSONArray) execution.getVariable(codeMappingPvName);
        if (codeMappings == null) {
            throw new BpmnError("Response code mapping missing");
        }

        String responsePayload = (String) execution.getVariable("responsePayload");
        if (StringUtils.isEmpty(responsePayload)) {
            execution.setVariable(SUCCESS_FLAG, false);
            return;
        }
        JSONObject response = (JSONObject) JSONSerializer.toJSON(responsePayload);

        if (!response.containsKey(RESPONSE_CODE)) {
            throw new BpmnError(String.format("Invalid response received,"
                 + " as response code is missing. Received response: %s", response.toString()));
        }
        String responseCode = response.getString(RESPONSE_CODE);
        execution.setVariable(RECEIVED_CODE, responseCode);
        if (response.containsKey(EXTERNAL_ID)) {
            execution.setVariable(EXTERNAL_ID, response.getString(EXTERNAL_ID));
        }

        interpretResponseCode(execution, codeMappings, reqCategory, responseCode);
        
        execution.setVariable("responsePayload", responsePayload);
    }

    private static void interpretResponseCode(DelegateExecution execution, JSONArray codeMappings,
            final String reqCategory, String responseCode) {
        String receivedCodeDisplayName = "Unknown Code";
        String receivedCodeLoggingRequired = "Yes";
        String codeType = "other";
        boolean successFlag = false;

        for (int i = 0; i < codeMappings.size(); ++i) {
            JSONObject mapping = codeMappings.getJSONObject(i);
            if (mapping == null) {
                continue;
            }
            String codeValue = mapping.getString("codeValue");
            String codeCategory = mapping.getString("codeCategory");
            if (reqCategory.equalsIgnoreCase(codeCategory) && responseCode.equalsIgnoreCase(codeValue)) {
                receivedCodeDisplayName = mapping.getString("codeDisplayName");
                receivedCodeLoggingRequired = mapping.getString("codeLoggingRequired");
                execution.setVariable("receivedCodeSubProcessId", mapping.getString("codeSubProcessId"));
                codeType = mapping.getString("codeType");
                successFlag = !"failure".equalsIgnoreCase(codeType);
                break;
            }
        }

        execution.setVariable("receivedCodeDisplayName", receivedCodeDisplayName);
        execution.setVariable("receivedCodeLoggingRequired", receivedCodeLoggingRequired);
        execution.setVariable(SUCCESS_FLAG, successFlag);
        execution.setVariable("receivedCodeType", codeType);
    }

}
