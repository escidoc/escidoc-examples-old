/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or http://www.escidoc.de/license.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright 2006-2008 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.  
 * All rights reserved.  Use is subject to license terms.
 */
package org.escidoc.simpleConnections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.escidoc.Constants;

import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

/**
 * @author Frank Schwichtenberg <Frank.Schwichtenberg@FIZ-Karlsruhe.de>
 * 
 */
public class Util {

    /*
     * public fields
     */

    public static final String DEFAULT_CONTENT_ENCODING = "UTF-8";

    public static final Object LINE_SEPARATOR =
        System.getProperty("line.separator");

    /*
     * private fields
     */

    private static String escidocInfrastructureHost =
        Constants.DEFAULT_INFRASTRUCTURE_HOST;

    private static String escidocInfrastructurePort =
        Constants.DEFAULT_INFRASTRUCTURE_PORT;

    private static String escidocInfrastructurePath =
        Constants.DEFAULT_INFRASTRUCTURE_PATH;

    /**
     * constructors
     */

    private Util() {
    }

    /*
     * getter and setter
     */

    public static String getEscidocInfrastructureHost() {
        return escidocInfrastructureHost;
    }

    public static void setEscidocInfrastructureHost(
        String escidocInfrastructureHost) {
        Util.escidocInfrastructureHost = escidocInfrastructureHost;
    }

    public static String getEscidocInfrastructurePort() {
        return escidocInfrastructurePort;
    }

    public static void setEscidocInfrastructurePort(
        String escidocInfrastructurePort) {
        Util.escidocInfrastructurePort = escidocInfrastructurePort;
    }

    /*
     * public methods
     */

    public static String getInfrastructureURL() {
        return "http://" + escidocInfrastructureHost + ":"
            + escidocInfrastructurePort + escidocInfrastructurePath;
    }

    public static String getXmlFileAsString(String path) throws IOException {
        return getXmlFileAsString(new File(path));
    }

    public static String getXmlFileAsString(File file) throws IOException {
        StringWriter writer = new StringWriter();
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(new FileInputStream(file),
                "UTF-8"));
        String line = reader.readLine();
        while (line != null) {
            writer.append(line);
        }
        reader.close();

        return writer.toString();
    }

    public static String getAuthHandle(String username, String password)
        throws AuthenticationException {
        String handle = null;

        try {

            URL loginUrl = new URL(getInfrastructureURL() + "/aa/login");
            URL authURL =
                new URL(getInfrastructureURL() + "/aa/j_spring_security_check");

            HttpURLConnection.setFollowRedirects(false);

            // 1) Make a login request in order to get the session cookie.
            HttpURLConnection restrictedConn =
                (HttpURLConnection) loginUrl.openConnection();
            restrictedConn.connect();
            // System.err.println(restrictedConn.getHeaderFields().get("Set-Cookie"));

            // 2) Make a POST request sending the login credentials and the
            // previous session cookie in order to get the next session cookie.
            HttpURLConnection authConn =
                (HttpURLConnection) authURL.openConnection();
            authConn.setRequestMethod("POST");
            authConn.setDoOutput(true);

            authConn.setRequestProperty("Cookie", restrictedConn
                .getHeaderField("Set-Cookie"));
            String params =
                "j_username=" + username + "&j_password=" + password;

            OutputStreamWriter w =
                new OutputStreamWriter(authConn.getOutputStream());
            w.write(params);
            w.close();

            authConn.connect();
            List<String> cookieList =
                authConn.getHeaderFields().get("Set-Cookie");
            // System.err.println(cookieList);

            // 3) Make a login request with the previous session cookie in order
            // to get the auth cookie.
            HttpURLConnection redirectConn =
                (HttpURLConnection) loginUrl.openConnection();
            if (cookieList != null) {
                Iterator<String> cookieIt = cookieList.iterator();
                while (cookieIt.hasNext()) {
                    redirectConn.addRequestProperty("Cookie", cookieIt.next());
                }
            }
            redirectConn.connect();
            cookieList = redirectConn.getHeaderFields().get("Set-Cookie");
            // System.err.println(cookieList);

            // check if login failed
            if (cookieList == null) {
                throw new AuthenticationException(403, "Forbidden",
                    "Could not log in user " + username + ".",
                    "redirectlocation");
            }
            // Get the user handle from the auth cookie.
            Iterator<String> cookieIterator = cookieList.iterator();
            while (cookieIterator.hasNext()) {
                String cookie = cookieIterator.next();
                if (cookie.toLowerCase().startsWith("escidoccookie=")) {
                    String[] parts = cookie.split(";");
                    handle = parts[0].split("=")[1];
                }
            }
            // System.err.println("Handle: " + handle);

        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return handle;
    }

    /**
     * Returns a reader for reading the content of url. Supported protocols are
     * "file" and those supported by <code>java.net.URLConnection</code>.
     * 
     * @param url
     * @return
     */
    public static BufferedReader getTemplate(URL url)
        throws AuthenticationException {
        return getTemplate(url, null, null);
    }

    /**
     * Returns a reader for reading the content of url. A HTTP Request to the
     * eSciDoc Infrastructure is authenticated with user and pass. Supported
     * protocols are "file" and those supported by
     * <code>java.net.URLConnection</code>.
     * 
     * @param url
     * @return
     * @throws AuthenticationException
     */
    public static BufferedReader getTemplate(URL url, String user, String pass)
        throws AuthenticationException {
        BufferedReader templateReader = null;

        try {

            if (url.getProtocol().equalsIgnoreCase("file")) {
                String path = url.toString().replaceFirst("file://", "");
                // TODO get encoding from XML header
                templateReader =
                    new BufferedReader(new InputStreamReader(
                        new FileInputStream(path), "UTF-8"));
            }
            else {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
                if (user != null && pass != null) {
                    conn.setRequestProperty("Cookie", "escidocCookie="
                        + Util.getAuthHandle(user, pass));
                }
                // check response code
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Error connecting template '" + url
                        + "'. Status: " + conn.getResponseCode() + ", "
                        + conn.getResponseMessage());
                }

                String contentEncoding = conn.getContentEncoding();
                if (contentEncoding == null) {
                    contentEncoding = DEFAULT_CONTENT_ENCODING;
                }

                templateReader =
                    new BufferedReader(new InputStreamReader(conn
                        .getInputStream(), contentEncoding));
                // String line = templateReader.readLine();
                // while (line != null) {
                // System.out.println(line);
                // line = templateReader.readLine();
                // }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(
                "Error retrieving template " + url + ".", e);
        }

        return templateReader;
    }
}
