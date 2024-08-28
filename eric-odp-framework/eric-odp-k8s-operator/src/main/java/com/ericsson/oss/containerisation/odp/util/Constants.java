/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2024
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.containerisation.odp.util;

import java.text.MessageFormat;

public class Constants {

    public static final String HYPHEN = "-";
    public static final String UNDERSCORE = "_";
    public static final String COLON = ":";
    public static final String SEMI_COLON = ";";
    public static final String MESSAGE_RESPONSE_TEMPLATE = "'{'\"message\":\"{0} for given userName:{1}\"'}'";
    public static final MessageFormat MESSAGE_JSON_FORMAT = new MessageFormat(MESSAGE_RESPONSE_TEMPLATE);
    public static final String SUCCESS_MESSAGE = "Successfully added pod creation request";
    public static final String POD_ALREADY_EXISTS = "Pod already exists";
    public static final String ERROR_MESSAGE = "Request to create pod already exists";
    public static final String POD_EXISTS_MESSAGE_RESPONSE_TEMPLATE = "'{'\"message\":\"{0} for given userName:{1}\",\"podName\":\"{2}\",\"podState\":\"{3}\"'}'";
    public static final MessageFormat POD_EXISTS_MESSAGE_JSON_FORMAT = new MessageFormat(POD_EXISTS_MESSAGE_RESPONSE_TEMPLATE);

    public static final String APP_LABEL = "app";
}
