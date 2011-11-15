package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.io.File;
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
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.interfaces.StagingHandlerClientInterface;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.StorageType;
import de.escidoc.core.resources.om.item.component.Component;
import de.escidoc.core.resources.om.item.component.ComponentContent;
import de.escidoc.core.resources.om.item.component.ComponentProperties;
import de.escidoc.core.resources.om.item.component.Components;

/**
 * Example how to create an Item.
 * 
 * @author SWA
 * 
 */
public class CreateItemWithContent {

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
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
		ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
		ihc.setHandle(auth.getHandle());

		Item item = new Item();

		item.getProperties().setContext(
				new ContextRef("escidoc:2"));
		item.getProperties().setContentModel(
				new ContentModelRef("escidoc:3"));

		// Metadata Record(s)
		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdrecord = getMdRecord("escidoc");
		mdRecords.add(mdrecord);
		item.setMetadataRecords(mdRecords);

		// upload local file to staging service
	    StagingHandlerClientInterface sthc = new StagingHandlerClient(
            auth.getServiceAddress());
	    sthc.setHandle(auth.getHandle());
	    URL stf = sthc.upload(new File("templates/generic/img/escidoc-logo.jpg"));
	    
	    // create a Component
		Component c = new Component();
		ComponentContent cc = new ComponentContent();
        cc.setXLinkHref(stf.toString());
		c.setContent(cc);
		cc.setStorage(StorageType.INTERNAL_MANAGED);

		// set some Component properties
		ComponentProperties cp = new ComponentProperties();
		cp.setVisibility("public");
		cp.setContentCategory("blafasel");
		cp.setMimeType("image/jpeg");
		c.setProperties(cp);

		Components cs = new Components();
		cs.add(c);
		item.setComponents(cs);
		
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

		MetadataRecord mdRecord = new MetadataRecord(name);

		Element element = doc.createElementNS(null, "myMdRecordAndMore");
		mdRecord.setContent(element);

		return mdRecord;
	}

}
