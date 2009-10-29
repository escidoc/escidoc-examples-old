package org.escidoc.workingWithClientLib.ClassMapping.item;

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
 * Example how to release an Item.
 * 
 * @author FRS
 * 
 */
public class ReleaseItem {

	/**
	 * 
	 * @param args
	 *            The objid of the to release Item.
	 */
	public static void main(String[] args) {

		try {

			/*
			 * Either set the objid of the Item by parameter or set the objid in
			 * the following id variable.
			 */
			String id = "escidoc:1";
			if (args.length > 0) {
				id = args[0];
			}

			releaseItem(id);

		} catch (EscidocClientException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Release an Item.
	 * 
	 * <p>
	 * Release an Item is a task oriented method and needs and additional
	 * parameter instead of the Item representation.
	 * </p>
	 * <p>
	 * The taskParam has to contain the last-modification-date of the Item
	 * (optimistic locking) and the release comment.
	 * </p>
	 * 
	 * @param id
	 *            The objid of the Item.
	 * @throws EscidocClientException
	 */
	public static void releaseItem(final String id)
			throws EscidocClientException {

		// prepare client object
		ItemHandlerClient ihc = new ItemHandlerClient();
		ihc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		// create item object retrieving the item
		Item item = ihc.retrieve(id);

		// release using submit result
		Result releaseResult = ihc.release(item, new TaskParam(item
				.getLastModificationDate(), "release", null, null,
				new Vector<Filter>()));

		System.out.println("Item with objid='" + id + "' at '"
				+ releaseResult.getLastModificationDateAsString()
				+ "' released.");
	}
}
