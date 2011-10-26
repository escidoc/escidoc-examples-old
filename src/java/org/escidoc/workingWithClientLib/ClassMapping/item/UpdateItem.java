package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.om.item.Item;

/**
 * Show how to update an Item in the infrastructure using Java objects.
 * 
 * @author SWA
 * 
 */
public class UpdateItem {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String id = "escidoc:1";

		try {
			Item item = retrieveItem(id);
			System.out.println("Item with objid='" + item.getObjid() + "' at '"
					+ item.getLastModificationDate() + "' in version no. "
					+ item.getProperties().getVersion().getNumber()
					+ " retrieved.");

			// add a meta data record to the Item
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element element = doc.createElementNS(null, "an_empty_element");

			MetadataRecord mdRecord = new MetadataRecord(
					"further_meta_data_record");
			mdRecord.setContent(element);

			item.getMetadataRecords().add(mdRecord);

			item = updateItem(item);
			System.out.println("Item with objid='" + item.getObjid() + "' at '"
					+ item.getLastModificationDate()
					+ "' updated to version no. "
					+ item.getProperties().getVersion().getNumber() + ".");

		} catch (EscidocClientException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update an Item in the infrastructure
	 * 
	 * @param item
	 *            The Item to update
	 * @return updated Item
	 * 
	 * @throws EscidocClientException
	 * @throws MalformedURLException
	 */
	private static Item updateItem(final Item item)
			throws EscidocClientException, MalformedURLException {

		// get authentication
		Authentication auth = new Authentication(new URL(
				Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_DEPOSITOR,
				Constants.USER_PASSWORD_DEPOSITOR);

		// get service handler
		ItemHandlerClient client = new ItemHandlerClient(
				auth.getServiceAddress());
		client.setHandle(auth.getHandle());

		return client.update(item);
	}

	/**
	 * Retrieve an Item from the infrastructure.
	 * 
	 * @param itemId
	 *            objid of the Item
	 * @return Item resource
	 * 
	 * @throws EscidocClientException
	 * @throws MalformedURLException
	 */
	private static Item retrieveItem(final String itemId)
			throws EscidocClientException, MalformedURLException {

		// get authentication
		Authentication auth = new Authentication(new URL(
				Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_DEPOSITOR,
				Constants.USER_PASSWORD_DEPOSITOR);

		// get service handler
		ItemHandlerClient client = new ItemHandlerClient(
				auth.getServiceAddress());
		client.setHandle(auth.getHandle());

		return client.retrieve(itemId);
	}

}
