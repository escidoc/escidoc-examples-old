package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.component.Component;
import de.escidoc.core.resources.om.item.component.ComponentContent;
import de.escidoc.core.resources.om.item.component.ComponentProperties;
import de.escidoc.core.resources.om.item.component.Components;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Item with binary content.
 * 
 * The binary content is to transfer from the local disk into the
 * infrastructure. The infrastructure requires HTTP access to pull files - which
 * is usually not given for local files. That's why the file is upload to the
 * staging service first. The HTTP reference of the uploaded file from the
 * staging service is used as content references in the Item.
 * 
 * @author SWA
 * 
 */
public class CreateItemWithBinaryContent {

    /**
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        try {
            // authentication (Use a user account with write permission for Item
            // on the selected Context. Usually is this user with depositor
            // role).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // upload local file
            File file = createFileWithRandomContent();
            URL contentRef = uploadLocalFile(auth, file);

            Item createdResource = createItem(auth, contentRef);

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
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create temp file with random content.
     * 
     * @return
     * @throws IOException
     */
    private static File createFileWithRandomContent() throws IOException {

        File temp =
            File.createTempFile("escidoc-binary-content-example", ".tmp");
        temp.deleteOnExit();

        // Write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));

        out.write("A random String " + System.nanoTime());
        out.close();

        return temp;
    }

    /**
     * Upload local file to staging service.
     * 
     * @param auth
     *            valid Authentication
     * @param file
     *            The file
     * @return URL of file at staging service
     * 
     * @throws EscidocException
     * @throws InternalClientException
     * @throws TransportException
     */
    private static URL uploadLocalFile(
        final Authentication auth, final File file) throws EscidocException,
        InternalClientException, TransportException {

        StagingHandlerClient sthc = new StagingHandlerClient();
        sthc.setServiceAddress(auth.getServiceAddress());
        sthc.setHandle(auth.getHandle());

        return sthc.upload(file);
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
    private static Item createItem(
        final Authentication auth, final URL contentRef)
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

        Component component = new Component();
        ComponentContent content = new ComponentContent();
        content.setHref(contentRef.toString());
        content.setStorage("internal-managed");
        component.setContent(content);
        component.setProperties(new ComponentProperties());
        component.getProperties().setDescription("Random content");
        component.getProperties().setFileName(contentRef.getFile());
        component.getProperties().setVisibility("public");
        component.getProperties().setContentCategory("pre-print");

        Components components = new Components();
        components.add(component);
        item.setComponents(components);

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
