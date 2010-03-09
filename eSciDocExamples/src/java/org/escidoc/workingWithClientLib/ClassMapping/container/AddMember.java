package org.escidoc.workingWithClientLib.ClassMapping.container;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.properties.ContentModelSpecific;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.om.item.Item;

/**
 * Example how to add a member to a Container.
 * 
 * @author SWA
 * 
 */
public class AddMember {

    /**
     * One Container and one Item are created. Then is the Item set as member of
     * the created Container.
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        try {

            // Container
            Container container = createContainer();

            System.out.println("Container with objid='" + container.getObjid()
                + "' at '" + container.getLastModificationDateAsString()
                + "' created.");

            // Item
            Item item = createItem();

            System.out.println("Item with objid='" + item.getObjid() + "' at '"
                + item.getLastModificationDateAsString() + "' created.");

            // add Item as member of the Container
            container = addMember(container, item);

            System.out.println("Container with objid='" + container.getObjid()
                + "' last modified at '"
                + container.getLastModificationDateAsString() + "' has "
                + container.getMembers().size() + " members.");

            Iterator<ResourceRef> it = container.getMembers().iterator();
            while (it.hasNext()) {
                System.out
                    .println("Member with objid: " + it.next().getObjid());
            }

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the Item as Member of the Container.
     * 
     * <p>
     * Attention: Members are not added through the Container representation
     * itself. It's done via the task oriented method 'addMember()'.
     * </p>
     * 
     * @param container
     *            The Container
     * @param item
     *            The Item which is to set as member of the Container
     * @return The updated Container
     * 
     * @throws EscidocException
     * @throws InternalClientException
     * @throws TransportException
     */
    private static Container addMember(
        final Container container, final Item item) throws EscidocException,
        InternalClientException, TransportException {

        // task oriented methods take values via task param
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(container.getLastModificationDate());
        taskParam.addResourceRef(item.getObjid());

        // login
        ContainerHandlerClient chc = new ContainerHandlerClient();
        chc.login(Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // call addMembers method
        chc.addMembers(container.getObjid(), taskParam);

        Container updatedContainer = chc.retrieve(container.getObjid());

        return updatedContainer;
    }

    /**
     * Create Container.
     * 
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Container createContainer() throws EscidocClientException,
        ParserConfigurationException {

        Container container = new Container();

        container.getProperties().setContext(
            new ResourceRef(Constants.EXAMPLE_CONTEXT_ID));
        container.getProperties().setContentModel(
            new ResourceRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Content-model-specific
        ContentModelSpecific cms = getContentModelSpecific();
        container.getProperties().setContentModelSpecific(cms);

        // Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        MetadataRecord mdrecord = getMdRecord("escidoc");
        mdRecords.add(mdrecord);
        container.setMetadataRecords(mdRecords);

        // login
        ContainerHandlerClient chc = new ContainerHandlerClient();
        chc.login(Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // create
        Container newContainer = chc.create(container);

        return newContainer;
    }

    /**
     * Create Item.
     * 
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Item createItem() throws EscidocClientException,
        ParserConfigurationException {

        Item item = new Item();

        item.getProperties().setContext(
            new ResourceRef(Constants.EXAMPLE_CONTEXT_ID));
        item.getProperties().setContentModel(
            new ResourceRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Content-model
        ContentModelSpecific cms = getContentModelSpecific();
        item.getProperties().setContentModelSpecific(cms);

        // Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        MetadataRecord mdrecord = getMdRecord("escidoc");
        mdRecords.add(mdrecord);
        item.setMetadataRecords(mdRecords);

        // login
        ItemHandlerClient ihc = new ItemHandlerClient();
        ihc.login(Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // create
        Item newItem = ihc.create(item);

        return newItem;
    }

    /**
     * Get content model specific.
     * 
     * @return
     * @throws ParserConfigurationException
     */
    private static ContentModelSpecific getContentModelSpecific()
        throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element contentModelSpecific = doc.createElementNS(null, "cms");
        Element element1 = doc.createElement("some-other-stuff");
        element1.setTextContent("some content - " + System.nanoTime());

        List<Element> cmsContent = new LinkedList<Element>();
        cmsContent.add(contentModelSpecific);
        cmsContent.add(element1);

        ContentModelSpecific cms = new ContentModelSpecific();
        cms.setContent(cmsContent);

        return cms;
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
