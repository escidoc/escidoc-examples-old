package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestOrganizationalUnitHandlerClient;

/**
 * Example how to create an Organizational Unit by using the XML REST
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
public class CreateOrganizationalUnit {

	/**
	 * @param args
	 *            args are ignored
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
	 * Create Organizational Unit.
	 * 
	 * <p>
	 * This method makes usage of a Content Relation XML (REST) template.<br/>
	 * objid of created Content Relation and the last modification date is
	 * finally printed to stdout.
	 * </p>
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	private static void create() throws InternalClientException,
			EscidocException, TransportException, IOException {

		// get handler for organizational units
		RestOrganizationalUnitHandlerClient rouc = new RestOrganizationalUnitHandlerClient();

		// authenticate
		rouc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// load XML template of organizational unit
		File templOu = new File("./templates/TUE/organizational-unit/"
				+ "escidoc_ou_for_create.xml");
		String ouXml = Util.getXmlFileAsString(templOu);

		// create
		String crtdOuXML = rouc.create(ouXml);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(crtdOuXML);
		System.out.println("Organizational Unit with objid='" + objidLmd[0]
				+ "' at '" + objidLmd[1] + "' created");
	}

}
