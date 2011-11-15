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
package org.escidoc.simpleConnections.container;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

/**
 * @author Frank Schwichtenberg <Frank.Schwichtenberg@FIZ-Karlsruhe.de>
 * 
 */
public class CreateContainer {

    private static String templateUrl =
        "http://localhost:8080/ir/container/escidoc:ex7";

    private static final String CREATE_CONTAINER_PATH = "/ir/container";

    private static String user;

    private static String pass;

    /**
     * Execute the creation of a resource in eSciDoc Infrastructure from given
     * XML as given user.
     * 
     * @param args
     *            An optional URL of an eSciDoc resource XML followed by
     *            username and password for login. If neither the URL nor the
     *            user credentials are present default values are applied.
     */
    public static void main(String[] args) {
        String xml = null;

        // first param is template url, if present
        if (args.length > 0) {
            templateUrl = args[0];
        }

        // set username and password from params or to default
        if (args.length < 3) {
            user = Constants.USER_NAME_SYSADMIN;
            pass = Constants.USER_PASSWORD_SYSADMIN;
        }
        else {
            user = args[1];
            pass = args[2];
        }

        // define the location of the template resource
        URL url = null;
        try {
            url = new URL(templateUrl);

            // create new resource from template
            xml = createFromTemplate(url);
        }
        catch (MalformedURLException e) {
            System.err.println("Mal formed URL '" + url + "'");
            System.exit(1);
        }
        catch (AuthenticationException e) {
            System.err.println("Error during authentication.");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(xml);

    }

    /**
     * Creates a resource in eSciDoc Infrastructure from template retrieved from
     * url. Supported protocols are "file" and those supported by
     * <code>java.net.URLConnection</code>.
     * 
     * @param url
     *            URL of template.
     * @return A string containing the XML representation of the newly created
     *         resource.
     * @throws AuthenticationException
     */
    private static String createFromTemplate(final URL url)
        throws AuthenticationException {

        BufferedReader in = Util.getTemplate(url, user, pass);

        String container = createContainer(in);

        return container;
    }

    /**
     * Creates a resource in eSciDoc Infrastructure from template read from
     * given Reader.
     * 
     * @param in
     *            Reader to read the template XML from.
     * @return A string containing the XML representation of the newly created
     *         resource.
     * @throws AuthenticationException
     */
    private static String createContainer(BufferedReader in)
        throws AuthenticationException {

        StringBuffer result = new StringBuffer();

        try {
            URL createUrl =
                new URL("http://" + Util.getEscidocInfrastructureHost() + ":"
                    + Util.getEscidocInfrastructurePort()
                    + CREATE_CONTAINER_PATH);
            HttpURLConnection createConnection =
                (HttpURLConnection) createUrl.openConnection();

            createConnection.setRequestProperty("Cookie", "escidocCookie="
                + Util.getAuthHandle(user, pass));

            createConnection.setRequestMethod("PUT");
            createConnection.setDoOutput(true);

            // write template to POST Request
            BufferedWriter out =
                new BufferedWriter(new OutputStreamWriter(createConnection
                    .getOutputStream(), Util.DEFAULT_CONTENT_ENCODING));
            String line = in.readLine();
            while (line != null) {
                out.write(line);
                line = in.readLine();
            }
            in.close();
            out.close();
            createConnection.connect();

            // connect creating reader
            BufferedReader createdReader = null;
            String contentEncoding = createConnection.getContentEncoding();
            if (contentEncoding == null) {
                contentEncoding = Util.DEFAULT_CONTENT_ENCODING;
            }
            try {
                createdReader =
                    new BufferedReader(new InputStreamReader(createConnection
                        .getInputStream(), contentEncoding));
            }
            catch (Exception e) {
                // write error xml to std.err
                e.printStackTrace();
                BufferedReader r =
                    new BufferedReader(new InputStreamReader(createConnection
                        .getErrorStream(), Util.DEFAULT_CONTENT_ENCODING));
                line = r.readLine();
                while (line != null) {
                    System.err.println(line);
                    line = r.readLine();
                }
                throw new RuntimeException("Creating resource failed.");
            }

            // read newly created resource
            line = createdReader.readLine();
            while (line != null) {
                result.append(line);
                result.append(Util.LINE_SEPARATOR);
                line = createdReader.readLine();
            }

            createdReader.close();
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result.toString();
    }

}
