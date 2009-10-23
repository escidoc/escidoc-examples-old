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
 * Example how to create a user account by using the XML REST
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
public class CreateUserAccount {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			create();
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
	 * Create Organizational Unit (from REST XML template).
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	private static void create() throws InternalClientException,
			EscidocException, TransportException, IOException {

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
		System.out.println("User Account with objid='" + objidLmd[0] + "' at '"
				+ objidLmd[1] + "' created");
	}

}