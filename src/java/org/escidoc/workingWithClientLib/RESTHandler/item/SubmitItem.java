package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.util.Vector;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;

/**
 * Example how to submit an Item.
 * 
 * @author FRS
 * 
 */
public class SubmitItem {

	/**
	 * 
	 * @param args
	 *            The objid of the to submit Item.
	 */
	public static void main(String[] args) {

        String id = "escidoc:1";
        if (args.length > 0) {
            id = args[0];
        }

		try {
			submitItem(id);
		} catch (EscidocClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 * @throws EscidocClientException
	 */
	public static void submitItem(final String id)
			throws EscidocClientException {

		// prepare client object
		ItemHandlerClient ihc = new ItemHandlerClient();
		ihc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// create item object retrieving the item
		Item item = ihc.retrieve(id);

		// prepare taskParam and call submit
		Result submitResult = ihc.submit(item, new TaskParam(item
				.getLastModificationDate(), "submit", null, null,
				new Vector<Filter>()));

		System.out.println("Item with objid='" + id + "' at '"
				+ submitResult.getLastModificationDateAsString()
				+ "' submitted.");
	}
}
