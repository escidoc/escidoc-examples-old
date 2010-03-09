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
 * Example how to create a Container.
 * 
 * @author SWA
 * 
 */
public class CreateContainer {

    /**
     * Create a Container at the infrastructure.
     * 
     * @param args
     *            ignored
     */
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
     * Create a Container and store it within infrastructure.
     * 
     * @return Container
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

        // Content-model
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
