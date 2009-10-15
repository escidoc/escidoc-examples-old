package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestOrganizationalUnitHandlerClient;
import de.escidoc.core.client.rest.RestUserAccountHandlerClient;

/**
 * Example how to handle user accounts by using the XML REST
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
public class UserAccountEx {

	/**
     * 
     */
	UserAccountEx() {
	}

	/**
	 * Create Organizational Unit (from REST XML template).
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	public void create() throws InternalClientException, EscidocException,
			TransportException, IOException {

		// get handler for organizational units
		RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient();

		// authenticate
		ruahc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// load XML template of organizational unit
		File templ = new File("./templates/TUE/user-account/"
				+ "escidoc_useraccount_for_create.xml");
		String resourceXml = Util.getXmlFileAsString(templ);

		// create
		String crXML = ruahc.create(resourceXml);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(crXML);
		System.out.println("User Account with objid='" + objidLmd[0]
				+ "' at '" + objidLmd[1] + "' created");
	}

	/**
	 * Set grants for user.
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	public void createGrant(final String userAccountId, final String grant) throws InternalClientException,
			EscidocException, TransportException, IOException {
		
		RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient();

		// authenticate
		ruahc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);
		
		String crXML = ruahc.createGrant(userAccountId, grant);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(crXML);
		System.out.println("Grant set for User Account with objid='" + userAccountId
				+ "' at '" + objidLmd[1] + "'.");
}
}
