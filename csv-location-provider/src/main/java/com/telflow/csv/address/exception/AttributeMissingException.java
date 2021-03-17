/*
 * Copyright (c) 2010-2017 DGIT Consultants Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT Consultants Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.csv.address.exception;

import com.telflow.fabric.common.exception.FabricException;

/**
 * A custom exception for missing attributes in provided CIM fields array
 * 
 * @author Muhammad Rukunuzzaman
 *
 */
public class AttributeMissingException extends FabricException {

    public AttributeMissingException(String message, Object... objects) {
        super(message, objects);
    }
}
