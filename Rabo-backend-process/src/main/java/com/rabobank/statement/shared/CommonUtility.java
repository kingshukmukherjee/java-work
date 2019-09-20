package com.rabobank.statement.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * <h1>Common Utility class!</h1>
 * This programme is used for re-usable constants and functions
 *
 * @author Kingshuk Mukherjee - 233303
 * @version 1.0
 * @since 20/09/2019
 */

public class CommonUtility {
    public static Resource inputResource = new FileSystemResource("input/records.csv");

    public static Resource outputResource = new FileSystemResource("output/reconciliationReport.csv");

    public static final String REFERENCE = "Reference";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String DESCRIPTION = "Description";
    public static final String START_BALANCE = "Start Balance";
    public static final String MUTATION = "Mutation";
    public static final String END_BALANCE = "End Balance";
}
