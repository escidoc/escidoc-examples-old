package org.escidoc.workingWithClientLib.ClassMapping.container;

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
 * Example how to create a Container by using the class mapping feature of
 * eSciDoc Java client library.
 * 
 * @author SWA
 * 
 */
public class CreateContainer {

    public static void main(String[] args) {

        try {
            // authentication (Use a user account with write permission for Container
            // on the selected Context. Usually is this user with depositor
            // role).
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            Container createdResource = createContainer(auth);

            System.out.println("Container with objid='"
                + createdResource.getObjid() + "' at '"
                + createdResource.getLastModificationDate()
                + "' created.");
            
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
     * Create Container.
     * 
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Container createContainer(final Authentication auth) throws EscidocClientException,
        ParserConfigurationException {

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
