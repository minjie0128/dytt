
package com.alading.ee.fusion;

/**
 * Server configuration.
 */
public class ServerInfo {

    /**
     * Distinguish if it is release version or debug version.True： release;
     * False： debug
     */
    public static final boolean IS_RELEASE = true;

    /**
     * Configure if log is available in a release version.
     */
    public static boolean LOG_ENABLE_ON_RELEASE = true;

    /**
     * If block alading point exchange service
     */
    public static boolean EXCHANGE_SERVICE_AVAILABLE = true;

    public static String MD5Key;

    public static final String SERVICE_NAMESPACE = "http://tempuri.org/";

    /**
     * Web service URL.
     */
    public static String WEB_SERVICE_URL;

    public static String FILE_SERVER_URL;
}
