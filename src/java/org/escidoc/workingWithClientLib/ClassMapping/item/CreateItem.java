package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;

/**
 * Example how to create an Item.
 * 
 * @author SWA
 * 
 */
public class CreateItem {

	/**
	 * Default Item template is loaded if no file location is given as parameter
	 * 
	 * @param args
	 *            location of Item XML
	 * @throws ParserConfigurationException
	 */
	public static void main(String[] args) {

		try {

			Item createdResource = createItem();

			System.out.println("Item with objid='" + createdResource.getObjid()
					+ "' at '"
					+ createdResource.getLastModificationDate()
					+ "' created.");

		} catch (EscidocClientException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create Item.
	 * 
	 * @return XML representation of the created Item
	 * @throws EscidocClientException
	 * @throws ParserConfigurationException
	 * @throws MalformedURLException 
	 */
	private static Item createItem() throws EscidocClientException,
			ParserConfigurationException, MalformedURLException {
		
		// prepare client object
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
		ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
		ihc.setHandle(auth.getHandle());

		Item item = new Item();

		item.getProperties().setContext(
				new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
		item.getProperties().setContentModel(
				new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

		// Metadata Record(s)
		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdrecord = getMdRecord("escidoc");
		mdRecords.add(mdrecord);
		item.setMetadataRecords(mdRecords);

		// create
		Item newItem = ihc.create(item);

		return newItem;
	}

	/**
	 * Get MetadataRecord.
	 * 
	 * @param name
	 *            Name of md-record.
	 * @return MetadataRecord
	 * @throws ParserConfigurationException
	 */
	private static MetadataRecord getMdRecord(final String name)
			throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		MetadataRecord mdRecord = new MetadataRecord();
		mdRecord.setName(name);

		Element element = doc.createElementNS(null, "myMdRecord");
		mdRecord.setContent(element);

		return mdRecord;
	}

}
