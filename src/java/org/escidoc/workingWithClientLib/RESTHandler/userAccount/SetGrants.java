package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestUserAccountHandlerClient;

/**
 * Example how to add grants to an existing user account by using the XML REST
 * representation and the eSciDoc Java client library.
 * 
 * The eSciDoc Java client library is used for communication with framework.
 * Unused is mapping between Java classes and XML representations to explain the
 * XML data structure.
 * 
 * eSciDoc XML REST representation is used. Please keep that in mind, if you
 * adapt these examples for SOAP.
 * 
 * @author SWA
 * 
 */
public class SetGrants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String userAccountId = "escidoc:1123792";

			// prepare and load taskParam XML
			File templ = new File("./templates/TUE/user-account/"
					+ "escidoc_depositor_grant_for_create.xml");
			String grantXml = Util.getXmlFileAsString(templ);

			createGrant(userAccountId, grantXml);
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

	/**
	 * Set grants for user.
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	private static void createGrant(final String userAccountId,
			final String grant) throws InternalClientException,
			EscidocException, TransportException, IOException {

		RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient();

		// authenticate
		ruahc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		String crXML = ruahc.createGrant(userAccountId, grant);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(crXML);
		System.out.println("User Account with objid='" + userAccountId
				+ "' at '" + objidLmd[1] + "' created");
	}

}
