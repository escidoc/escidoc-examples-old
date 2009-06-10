package org.escidoc.workingWithClientLib.crud;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.properties.ContentModelSpecific;
import de.escidoc.core.resources.om.item.Item;

/**
 * Create eSciDoc resources with escidoc-core-client-library.
 * 
 * @author SWA
 * 
 */
public class Create {

	/**
	 * Create an Item.
	 * 
	 * @throws TransportException
	 *             Thrown in case of errors on transport level.
	 * @throws InternalClientException
	 *             Thrown if client library internal errors occur.
	 * @throws EscidocException
	 *             Thrown if framework throws exception.
	 * @throws ParserConfigurationException
	 */
	public void createItem() throws EscidocException, InternalClientException,
			TransportException, ParserConfigurationException {

		ItemHandlerClient cc = new ItemHandlerClient();
		cc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		Item item = new Item();

		item.getProperties().setContext(
				new ResourceRef(Constants.EXAMPLE_CONTEXT_ID));
		item.getProperties().setContentModel(
				new ResourceRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

		// // Content-model-specific
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element contentModelSpecific = doc.createElementNS(null, "cms");
		Element element1 = doc.createElement("some-other-stuff1");
		element1.setTextContent("33333333333333333333");

		List<Element> cmsContent = new LinkedList<Element>();
		cmsContent.add(contentModelSpecific);
		cmsContent.add(element1);
		ContentModelSpecific cms = new ContentModelSpecific();

		cms.setContent(cmsContent);

		item.getProperties().setContentModelSpecific(cms);

		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdRecord = new MetadataRecord();
		mdRecord.setName("escidoc");

		Element element = doc.createElementNS(null, "myMdRecord");
		mdRecord.setContent(element);

		mdRecords.add(mdRecord);
		item.setMetadataRecords(mdRecords);

		// Relations relations = new Relations();
		// Relation relation = new Relation(new ResourceRef(EXAMPLE_ITEM_ID));
		// relation
		// .setPredicate("http://www.escidoc.de/ontologies/mpdl-ontologies/content-relations#isPartOf");
		// relations.add(relation);
		// item.setRelations(relations);
		cc.create(item);

	}

}
