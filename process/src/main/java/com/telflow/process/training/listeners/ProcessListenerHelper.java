package com.telflow.process.training.listeners;

import biz.dgit.schemas.telflow.cim.v3.CharacteristicValues;
import biz.dgit.schemas.telflow.cim.v3.RootEntity;
import com.telflow.cim.util.CimPath;
import com.telflow.process.utils.PayloadContainer;
import com.telflow.process.utils.TelflowXMLProcessor;
import java.util.NoSuchElementException;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;

/**
 * Helper methods for Process Listeners.
 * 
 * @author Nik Ambukovski
 *
 */
public class ProcessListenerHelper {
    
    private ProcessListenerHelper() {
        
    }
    
    /**
     * Retrieves a process variable from an execution.
     * @param <T> the type of the process variable
     * @param name the process variable name
     * @param type the process variable type
     * @param execution a {@code DelegateExecution}
     * @return the process variable, or null if the process variable is not found
     * @throws ClassCastException if the process variable is not null and is not assignable to the type T.
     */
    public static <T extends Object> T getProcessVariable(DelegateExecution execution, String name, Class<T> type) {
        return type.cast(execution.getVariable(name));
    }
    
    /**
     * Retrieves a process variable from an execution. The process variable must be a non null value.
     * @param <T> the type of the process variable
     * @param execution a {@link DelegateExecution}
     * @param name the process variable name
     * @param type the process variable type
     * @return the process variable
     * @throws NoSuchElementException when the process variable is not found
     * @throws ClassCastException if the process variable is not null and is not assignable to the type T.
     */
    public static <T> T getRequiredProcessVariable(DelegateExecution execution, String name, Class<T> type) {
        Object obj = execution.getVariable(name);
        if (obj == null) {
            throw new NoSuchElementException(String.format(
                    "Process variable '%s' required but either not found or null.", name));
        }
        return type.cast(obj);
    }
    
    /**
     * Retrieves an expression value from an execution.
     * @param <T> the type of the expression value
     * @param execution a {@code DelegateExecution}
     * @param expression an {@code Expression}
     * @param type the {@code Expression} value type
     * @return the expression value, or null if the expression not supplied
     * @throws ClassCastException if the expression value is not null and is not assignable to the type T.
     */
    public static <T> T getExpressionValue(DelegateExecution execution, Expression expression, Class<T> type) {
        if (expression == null) {
            return null;
        }
        return type.cast(expression.getValue(execution));
    }
    
    /**
     * Retrieves an expression value from an execution. The expression value must be a non null value.
     * @param <T> the type of the expression value
     * @param execution a {@code DelegateExecution}
     * @param expression an {@code Expression}
     * @param type the {@code Expression} value type
     * @return the expression value
     * @throws NoSuchElementException when an expression value is not supplied
     * @throws NullPointerException when an expression value is null
     * @throws ClassCastException if the expression value is not null and is not assignable to the type T.
     */
    public static <T> T getRequiredExpressionValue(DelegateExecution execution, Expression expression, Class<T> type) {
        if (expression == null) {
            throw new NoSuchElementException("Expression value was not supplied.");
        }
        Object obj = expression.getValue(execution);
        if (obj == null) {
            throw new NullPointerException("Expression value was null.");
        }
        return type.cast(obj);
    }

    /**
     * Get Configuration Instance {@code CimPath} from process variable
     * @param execution {@code DelegateExecution}
     * @param configInstanceName process variable name containing Configuration instance
     * @return {@code CimPath} of the Configuration Instance
     */
    public static CimPath getConfigInstance(DelegateExecution execution, String configInstanceName) {
        PayloadContainer payload = (PayloadContainer) execution.getVariable(configInstanceName);
        if (payload == null) {
            throw new BpmnError("Config Instance could not be found");
        }

        CharacteristicValues charValues = TelflowXMLProcessor.getInstance()
            .unmarshal(payload.getPayload(), CharacteristicValues.class);
        return new CimPath(new RootEntity().withDescribedBy(charValues));
    }

    
}
