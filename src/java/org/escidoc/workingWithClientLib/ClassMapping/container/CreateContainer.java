package org.escidoc.workingWithClientLib.ClassMapping.container;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.properties.ContentModelSpecific;
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

            Container createdResource = createContainer();

            System.out.println("Container with objid='"
                + createdResource.getObjid() + "' at '"
                + createdResource.getLastModificationDateAsString()
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
     * Create Container.
     * 
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws ParserConfigurationException
     */
    private static Container createContainer() throws EscidocClientException,
        ParserConfigurationException {

        ContainerHandlerClient chc = new ContainerHandlerClient();
        chc.login(Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
            Constants.USER_PASSWORD);

        Container container = new Container();

        container.getProperties().setContext(
            new ResourceRef(Constants.EXAMPLE_CONTEXT_ID));
        container.getProperties().setContentModel(
            new ResourceRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Content-model
        ContentModelSpecific cms = getContentModelSpecific();
        container.getProperties().setContentModelSpecific(cms);

        // Metadata Record(s)
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
