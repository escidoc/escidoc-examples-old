package org.escidoc.workingWithClientLib.ClassMapping.container;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.om.item.Item;

/**
 * Example how to add members to a Container.
 * 
 * Precondition: A Container and the member(s) has to exists.
 * 
 * @author SWA
 * 
 */
public class AddMember {

    /**
     * 
     * @param args
     *            discarded
     */
    public static void main(String[] args) {

        try {

            // authentication (Use a user account with update permission for
            // the selected Container.
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // create a Container
            Container container = createContainer(auth);

            // create a set of members
            Item item1 = createItem(auth);
            Item item2 = createItem(auth);
            Container container2 = createContainer(auth);

            String[] members =
                new String[] { item1.getObjid(), item2.getObjid(),
                    container2.getObjid() };

            addMember(auth, container, members);
            
            auth.logout();

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add Member to a Container.
     * 
     * <p>
     * Adding member to a Container is a task oriented method and needs and
     * additional parameter instead of the Container representation.
     * </p>
     * <p>
     * The taskParam has to contain the last-modification-date of the Container
     * (optimistic locking) and the member reference(s) comment.
     * </p>
     * <p>
     * One can add multiple member in one step.
     * </p>
     * 
     * @param objid
     *            The objid of the Container.
     * @throws EscidocClientException
     */
    public static void addMember(
        final Authentication auth, final Container container,
        final String[] members) throws EscidocClientException {

        // get handler
        ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
        chc.setHandle(auth.getHandle());

        // prepare taskParam and call release
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(container.getLastModificationDate());
        taskParam.setComment("Members added");

        for (int i = 0; i < members.length; i++) {
            taskParam.addResourceRef(members[i]);
        }

        Result releaseResult = chc.addMembers(container.getObjid(), taskParam);

        System.out.println("Members to Container with objid='"
            + container.getObjid() + "' added at '"
            + releaseResult.getLastModificationDate() + "'.");
    }

    /**
     * Create Container.
     * 
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Container createContainer(final Authentication auth)
        throws EscidocClientException, ParserConfigurationException {

        ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
        chc.setHandle(auth.getHandle());

        Container container = new Container();

        // add properties
        container.getProperties().setContext(
            new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
        container.getProperties().setContentModel(
            new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // add Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        MetadataRecord mdrecord =
            getMdRecord("escidoc", "myMdRecord", "Exmaple Container",
                "Description of an example Container.");
        mdRecords.add(mdrecord);
        container.setMetadataRecords(mdRecords);

        // create
        Container newContainer = chc.create(container);

        return newContainer;
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

        ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
        ihc.setHandle(auth.getHandle());

        Item item = new Item();

        item.getProperties().setContext(
            new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
        item.getProperties().setContentModel(
            new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        MetadataRecord mdrecord =
            getMdRecord("escidoc", "myMdRecord", "Exmaple Item",
                "Description of an example Item.");
        mdRecords.add(mdrecord);
        item.setMetadataRecords(mdRecords);

        // create
        Item newItem = ihc.create(item);

        return newItem;
    }

    /**
     * Creates an Metadata Record with DC content.
     * 
     * @param mdRecordName
     *            Name of the MdRecord
     * @param rootElementName
     *            Name of the root element of the MdRecord content
     * @param title
     *            The title which is to set in the DC metadata
     * @param description
     *            The description which is to set in the DC metadata
     * @return The MetadataRecord with the given values
     * @throws ParserConfigurationException
     *             If instantiation of DocumentBuilder fail
     */
    private static MetadataRecord getMdRecord(
        final String mdRecordName, final String rootElementName,
        final String title, final String description)
        throws ParserConfigurationException {

        // md-record
        MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName(mdRecordName);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true);
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();
        Element mdRecordContent = doc.createElementNS(null, rootElementName);
        mdRecord.setContent(mdRecordContent);

        // title
        Element titleElmt =
            doc.createElementNS("http://purl.org/dc/elements/1.1/", "title");
        titleElmt.setPrefix("dc");
        titleElmt.setTextContent(title);
        mdRecordContent.appendChild(titleElmt);

        // dc:description
        Element descriptionElmt =
            doc.createElementNS("http://purl.org/dc/elements/1.1/",
                "description");
        descriptionElmt.setPrefix("dc");
        descriptionElmt.setTextContent(description);
        mdRecordContent.appendChild(descriptionElmt);
        mdRecord.setContent(mdRecordContent);

        return mdRecord;
    }

}
