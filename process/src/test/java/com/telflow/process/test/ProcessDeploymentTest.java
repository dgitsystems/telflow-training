package com.telflow.process.test;

import static org.junit.Assert.*;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import com.telflow.process.activiti.ForceFailActivitiRule;

/**
 * @author macbookpro2
 *
 */
public class ProcessDeploymentTest {

    @Test
    @Deployment(resources = { "product/MyExercise.bpmn" })
    public void GeneralProcessTest() {
        // noop
    }

}
