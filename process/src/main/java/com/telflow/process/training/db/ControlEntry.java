package com.telflow.process.training.db;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteraction;

import java.io.Serializable;

/**
 * A class to contain information about an entry of the ExecutionControlManager table
 */
public class ControlEntry implements Serializable {

    /**
     * The possible statuses for the Control Entry
     */
    public enum Status {
        /**
         * An entry is ready to be run immediately.
         */
        READY_TO_RUN,
        /**
         * An entry is waiting on a dependency to be completed.
         */
        DEPENDENCY,
        /**
         * An entry is queued up for execution.
         */
        EXECUTION_PENDING,
        /**
         * An entry is currently running.
         */
        RUNNING,
        /**
         * An entry completed with a success outcome.
         */
        COMPLETED,
        /**
         * An entry completed with a failure outcome.
         */
        FAILED
    }

    private String businessInteractionId;

    private String processId;

    private String mappingKey;

    private String runProcess;

    private String processNarrative;

    private Status status;

    private String executionIfRunning;

    private String processOutcome;

    private BusinessInteraction order;

    public String getBusinessInteractionId() {
        return businessInteractionId;
    }

    public ControlEntry withBusinessInteractionId(String value) {
        this.businessInteractionId = value;
        return this;
    }

    public void setBusinessInteractionId(String value) {
        this.businessInteractionId = value;
    }

    public String getProcessId() {
        return processId;
    }

    public ControlEntry withProcessId(String value) {
        this.processId = value;
        return this;
    }

    public void setProcessId(String value) {
        this.processId = value;
    }

    public String getProcessNarrative() {
        return processNarrative;
    }

    public ControlEntry withProcessNarrative(String value) {
        this.processNarrative = value;
        return this;
    }

    public void setProcessNarrative(String value) {
        this.processNarrative = value;
    }

    public String getMappingKey() {
        return mappingKey;
    }

    public ControlEntry withMappingKey(String value) {
        this.mappingKey = value;
        return this;
    }

    public void setMappingKey(String value) {
        this.mappingKey = value;
    }

    public String getRunProcess() {
        return runProcess;
    }

    public ControlEntry withRunProcess(String value) {
        this.runProcess = value;
        return this;
    }

    public void setRunProcess(String value) {
        this.runProcess = value;
    }

    public String getExecutionIfRunning() {
        return executionIfRunning;
    }

    public ControlEntry withExecutionIfRunning(String value) {
        this.executionIfRunning = value;
        return this;
    }

    public void setExecutionIfRunning(String value) {
        this.executionIfRunning = value;
    }

    public Status getStatus() {
        return status;
    }

    public ControlEntry withStatus(Status value) {
        this.status = value;
        return this;
    }

    public void setStatus(Status value) {
        this.status = value;
    }

    public String getProcessOutcome() {
        return processOutcome;
    }

    public ControlEntry withProcessOutcome(String value) {
        this.processOutcome = value;
        return this;
    }

    public void setProcessOutcome(String value) {
        this.processOutcome = value;
    }


    public BusinessInteraction getOrder() {
        return order;
    }

    public ControlEntry withOrder(BusinessInteraction value) {
        this.order = value;
        return this;
    }

    public void setOrder(BusinessInteraction value) {
        this.order = value;
    }
}
