package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

/**
 * Example how to create an Item.
 * 
 * @author FRS
 * 
 */
public class CreateItem {

	/**
	 * Default Item template is loaded if no file location is given as parameter
	 * 
	 * @param args
	 *            location of Item XML
	 */
	public static void main(String[] args) {

		try {

			String xmlFile = "templates/TUE/Item_create_minimal.xml";
			if (args.length > 0) {
				xmlFile = args[0];
			}

			String createdResource = createItem(Util
					.getXmlFileAsString(xmlFile));

			String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
			System.out.println("Item with objid='" + objidLmd[0] + "' at '"
					+ objidLmd[1] + "' created.");

		} catch (EscidocClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out
					.println("First parameter must be an eSciDoc Item XML File.");
		}
	}

	/**
	 * Create Item.
	 * 
	 * @param itemXml
	 *            Item XML
	 * @return XML representation of the created Item
	 * @throws EscidocClientException
	 */
	private static String createItem(final String itemXml)
			throws EscidocClientException {

		// get REST handler
		RestItemHandlerClient rihc = new RestItemHandlerClient();

		// login
		rihc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// create the Item
		String createdItem = rihc.create(itemXml);

		return createdItem;
	}

}