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
 * Example how to set the status of an Organizational Unit to open.
 * 
 * <p>
 * The status open is required to use the Orgaizational Unit; e.g. add an user
 * to this Organizational Unit.
 * </P>
 * 
 * @author SWA
 * 
 */
public class Open {

	/**
	 * @param args
	 *            args are ignored
	 */
	public static void main(String[] args) {

		try {
			/*
			 * Set the objid of the Organizational Unit which you want to set in
			 * status open.
			 */
			String objid = "escidoc:1124785";

			/*
			 * Prepare and load taskParam XML.
			 * 
			 * You have to add the last-modification-date of the Organizational
			 * Unit which is to set in status open into the taskParam XML
			 * template.
			 */
			File templ = new File("./templates/TUE/taskParam.xml");
			String taskParam = Util.getXmlFileAsString(templ);

			/*
			 * call task oriented method open with objid and taskParam
			 */
			open(objid, taskParam);

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
	private static void open(final String objid, final String taskParam)
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
