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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Frank Schwichtenberg <Frank.Schwichtenberg@FIZ-Karlsruhe.de>
 * 
 */
public class Create {

    private static final String TEMPLATE_URL =
        "http://localhost:8080/ir/item/escidoc:ex5";

    private static final String CREATE_ITEM_URL =
        "http://localhost:8080/ir/item";

    private static String userHandle = "Shibboleth-Handle-1";

    private static String user;

    private static String pass;

    /**
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            user = "roland";
            pass = "Shibboleth-Handle-1";
        }
        else {
            user = args[0];
            pass = args[1];
        }

        URL url = null;
        try {
            // define the location of the template resource
            url = new URL(TEMPLATE_URL);

            // create new resource from template
            String xml = createFromTemplate(url);

            System.out.println(xml);

        }
        catch (MalformedURLException e) {
            System.err.println("Mal formed URL '" + url + "'");
            System.exit(1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String createFromTemplate(final URL url) throws IOException {

        BufferedReader in = Util.getTemplate(url);

        String item = createItem(in);

        return item;
    }

    private static String createItem(BufferedReader in) {

        StringBuffer result = new StringBuffer();

        try {
            URL createUrl = new URL(CREATE_ITEM_URL);
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
                throw new RuntimeException(
                    "Create connection for creating resource failed.");
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
