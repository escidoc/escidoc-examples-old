package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.File;
import java.io.IOException;

import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class CreateGrant {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UserAccountEx uaEx = new UserAccountEx();

		try {
			String userAccountId = "escidoc:1";

			// prepare and load taskParam XML
			File templ = new File("./templates/TUE/user-account/"
					+ "escidoc_depositor_grant_for_create.xml");
			String grantXml = Util.getXmlFileAsString(templ);

			uaEx.createGrant(userAccountId, grantXml);
		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
