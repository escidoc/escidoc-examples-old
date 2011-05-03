package org.escidoc.workingWithClientLib.ClassMapping.container;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.container.Container;

/**
 * Example how to create an Item.
 * 
 * @author SWA
 * 
 */
public class CreateContainer {

	/**
	 * Default Item template is loaded if no file location is given as parameter
	 * 
	 * @param args
	 *            location of Item XML
	 * @throws ParserConfigurationException
	 */
	public static void main(String[] args) {

		try {
			
			Container createdResource = createContainer();

			System.out.println("Container with objid='" + createdResource.getObjid()
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
	 * Create Container.
	 * 
	 * @return XML representation of the created Item
	 * @throws EscidocClientException
	 * @throws ParserConfigurationException
	 * @throws MalformedURLException 
	 */
	private static Container createContainer() throws EscidocClientException,
			ParserConfigurationException, MalformedURLException {
		
		// prepare client object
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
		ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
		chc.setHandle(auth.getHandle());

		Container container = new Container();

		container.getProperties().setContext(
				new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
		container.getProperties().setContentModel(
				new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

		// Metadata Record(s)
		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdrecord = getMdRecord("escidoc");
		mdRecords.add(mdrecord);
		container.setMetadataRecords(mdRecords);

		// create
		Container newContainer = chc.create(container);

		return newContainer;
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
