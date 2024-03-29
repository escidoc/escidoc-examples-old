package org.escidoc;

public class Constants {

    public static final String DEFAULT_INFRASTRUCTURE_HOST = "localhost";

    public static final String DEFAULT_INFRASTRUCTURE_PORT = "8080";

    public static final String DEFAULT_INFRASTRUCTURE_PATH = "";

    public static final String DEFAULT_SERVICE_URL =
        "http://" + DEFAULT_INFRASTRUCTURE_HOST + ":"
            + DEFAULT_INFRASTRUCTURE_PORT + DEFAULT_INFRASTRUCTURE_PATH;
    
    public static final String USER_NAME_SYSADMIN = "sysadmin";
    public static final String USER_PASSWORD_SYSADMIN = "sysadmin";
    
    public static final String USER_NAME_INSPECTOR_ = "inspector";
    public static final String USER_PASSWORD_INSPECTOR = "inspector";
    
    public static final String USER_NAME_DEPOSITOR = "depositor";
    public static final String USER_PASSWORD_DEPOSITOR = "depositor";
    
    
    /**
     * Objid from common example set.
     */
    public static final String EXAMPLE_CONTEXT_ID = "escidoc:ex1";

    public static final String EXAMPLE_CONTENT_MODEL_ID = "escidoc:ex4";
}
