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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Retrieve from eSciDoc example.<br />
 * 
 * The code is kept as simple as possible to explain how one can retrieve data.
 * Because authentication is missing, suites this only for public available
 * resources.
 * 
 * @author SWA
 * 
 */
public class Retrieve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// define the location if the resource
		URL url = null;

		try {
			url = new URL("http://localhost:8080/ir/item/escidoc:ex5");
		} catch (MalformedURLException e) {
			System.err.println("Mal formed URL '" + url + "'");
			System.exit(1);
		}

		// establish connection
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// check response code
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.err.println(conn.getResponseMessage());
			} else {
				// write out data
				DataInputStream in = new DataInputStream(conn.getInputStream());

				int ch;
				while ((ch = in.read()) >= 0) {
					System.out.print((char) ch);
				}

				in.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
