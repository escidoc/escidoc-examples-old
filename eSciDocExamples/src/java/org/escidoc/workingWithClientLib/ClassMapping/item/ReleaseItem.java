package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
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
		} catch (MalformedURLException e) {
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
	 * @throws MalformedURLException 
	 */
	public static void releaseItem(final String id)
			throws EscidocClientException, MalformedURLException {
		
		// prepare client object
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_DEPOSITOR, Constants.USER_PASSWORD_DEPOSITOR);
		ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
		ihc.setHandle(auth.getHandle());

		// create item object retrieving the item
		Item item = ihc.retrieve(id);

		// release using submit result
		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(item.getLastModificationDate());
		taskParam.setComment("release");
		taskParam.setFilters(new Vector<Filter>());
		Result releaseResult = ihc.release(item, taskParam);
		
		System.out.println("Item with objid='" + id + "' at '"
				+ releaseResult.getLastModificationDate()
				+ "' released.");
	}
}
