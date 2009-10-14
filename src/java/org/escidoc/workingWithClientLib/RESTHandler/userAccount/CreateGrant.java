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

		UserAccountEx ouEx = new UserAccountEx();

		try {
			String userAccountId = "escidoc:1123792";

			// prepare and load taskParam XML
			File templ = new File("./templates/TUE/user-account/"
					+ "escidoc_depositor_grant_for_create.xml");
			String grantXml = Util.getXmlFileAsString(templ);

			ouEx.createGrant(userAccountId, grantXml);
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
