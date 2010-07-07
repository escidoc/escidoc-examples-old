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
 * Example how to update an Item.
 * 
 * <ul>
 * <li>A new Item is created and updated afterwards.</li>
 * <li>The configured user requires permissions to update the Item.</li>
 * </ul>
 * 
 * @author SWA
 */
public class UpdateItem {

    /**
     * 
     * @param args
     *            discarded
     */
    public static void main(String[] args) {

        try {

            // authentication (Use a user account with write permission for Item
            // on the selected Context. Usually is this user with depositor
            // role).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // create an Item
            Item item = createItem(auth);

            // change values of Item
            // attention: some values are fix, or cannot be changed by update
            // method (e.g. public-status)
            // add a further meta data set
            MetadataRecord mdRecord2 = new MetadataRecord();
            mdRecord2.setName("md-record2");
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element element = doc.createElementNS(null, "mySecondRecord");
            element.setTextContent("2222222222");
            mdRecord2.setContent(element);

            item.getMetadataRecords().add(mdRecord2);

            // update Item
            ItemHandlerClient ihc = new ItemHandlerClient();
            ihc.setServiceAddress(auth.getServiceAddress());
            ihc.setHandle(auth.getHandle());

            item = ihc.update(item);

            System.out.println("Item with objid='" + item.getObjid() + "' at '"
                + item.getLastModificationDate() + "' updated.");
            System.out.println("Item has now "
                + item.getMetadataRecords().size() + " metadata records.");

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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName("escidoc");

        Element element = doc.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        mdRecords.add(mdRecord);
        item.setMetadataRecords(mdRecords);

        // create
        Item newItem = ihc.create(item);

        return newItem;
    }

}
