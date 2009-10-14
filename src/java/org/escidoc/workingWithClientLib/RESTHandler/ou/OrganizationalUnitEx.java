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
 * Example how to handle an Organizational Unit by using the XML REST
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
public class OrganizationalUnitEx {

	/**
     * 
     */
	OrganizationalUnitEx() {
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

	/**
	 * Create Organizational Unit (from REST XML template) as child of other OU.
	 * 
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	public void createChildOu() throws InternalClientException,
			EscidocException, TransportException, IOException {

		// get handler for organizational units
		RestOrganizationalUnitHandlerClient rouc = new RestOrganizationalUnitHandlerClient();

		// authenticate
		rouc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// load XML template of organizational unit
		File templOu = new File("./templates/TUE/organizational-unit/"
				+ "escidoc_child_ou_for_create.xml");
		String ouXml = Util.getXmlFileAsString(templOu);

		// create
		String crtdOuXML = rouc.create(ouXml);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(crtdOuXML);
		System.out.println("Organizational Unit with objid='" + objidLmd[0]
				+ "' at '" + objidLmd[1] + "' created");
	}

	/**
	 * Set Organizational Unit to open.
	 * 
	 * @param objd
	 *            The objid of the Organizational Unit
	 * @param lmd
	 *            last modification date of Organizational Unit
	 * @throws InternalClientException
	 * @throws EscidocException
	 * @throws TransportException
	 * @throws IOException
	 */
	public void open(final String objid, final String taskParam)
			throws InternalClientException, EscidocException,
			TransportException, IOException {

		// login
		RestOrganizationalUnitHandlerClient rouc = new RestOrganizationalUnitHandlerClient();
		rouc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// open OU
		String responseXml = rouc.open(objid, taskParam);

		// write out objid and last modification date
		String[] objidLmd = Util.obtainObjidAndLmd(responseXml);
		System.out.println("Organizational Unit with objid='" + objid
				+ "' at '" + objidLmd[1] + "' set to open");
	}
}
