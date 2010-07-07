package org.escidoc.workingWithClientLib.ClassMapping.item;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.test.client.EscidocClientTestBase;

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
            // authentication (Use a user account with write permission for Item
            // on the selected Context. Usually is this user with depositor
            // role).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            Item createdResource = createItem(auth);

            System.out.println("Item with objid='" + createdResource.getObjid()
                + "' at '" + createdResource.getLastModificationDate()
                + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Item.
     * 
     * @param auth
     *            Authentication (a user with write permission to create an
     *            Item)
     * @return XML representation of the created Item
     * 
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Item createItem(final Authentication auth)
        throws EscidocClientException, ParserConfigurationException {

        ItemHandlerClient ihc = new ItemHandlerClient();
        ihc.setServiceAddress(auth.getServiceAddress());
        ihc.setHandle(auth.getHandle());

        Item item = new Item();

        item.getProperties().setContext(
            new ResourceRef(Constants.EXAMPLE_CONTEXT_ID));
        item.getProperties().setContentModel(
            new ResourceRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

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
