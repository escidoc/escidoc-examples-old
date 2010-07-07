package org.escidoc.workingWithClientLib.ClassMapping.ou;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Properties;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Organizational Unit by using the class mapping
 * feature of eSciDoc Java client library.
 * 
 * @author SWA
 * 
 */
public class CreateOu {

    /**
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        try {
            // Prepare a value object with new values of Organizational Unit
            OrganizationalUnit ou = prepareOrganizationalUnit();

            // call create with VO on eSciDoc
            ou = createOrganizationalUnit(ou);

            // for convenient reason: print out objid and last-modification-date
            // of created Organizational Unit
            System.out.println("Organizational Unit with objid='"
                + ou.getObjid() + "' at '" + ou.getLastModificationDate()
                + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The value object Organizational Unit is to create and to fill with (at
     * least required) parameter.
     * 
     * @return OrganizationalUnit (which is not created within the
     *         infrastructure).
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private static OrganizationalUnit prepareOrganizationalUnit()
        throws ParserConfigurationException, SAXException, IOException {

        OrganizationalUnit ou = new OrganizationalUnit();

        Properties properties = new Properties();
        ou.setProperties(properties);

        /*
         * An Organizational Unit requires title, alternative title(s) and
         * description. These values are set via metadata-record with name
         * 'escidoc'.
         */
        MetadataRecord mdRecord =
            getMdRecord("escidoc", "exampleDC", "Example Organizational Unit",
                "Description of an example Organizational Unit.");

        // add mdRecord to set
        MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(mdRecord);

        // add metadata-records to OU
        ou.setMetadataRecords(mdRecords);

        return ou;
    }

    /**
     * Creating the Organizational Unit within the eSciDoc infrastructure. The
     * value object OrganizationalUnit is send to the create method of the
     * infrastructure. The infrastructure delivers the created
     * OrganizationalUnit as response. The created OrganizationalUnit is
     * enriched with values from the infrastructure.
     * 
     * @param ou
     *            The value object of an OrganizationalUnit.
     * @return Value Object of created OrganizationalUnit (enriched with values
     *         by infrastructure)
     * @throws EscidocClientException
     */
    private static OrganizationalUnit createOrganizationalUnit(
        final OrganizationalUnit ou) throws EscidocClientException {

        // authentication (Use a user account with permission to create an
        // Organizational Unit).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get handler
        OrganizationalUnitHandlerClient client =
            new OrganizationalUnitHandlerClient();
        client.setServiceAddress(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        // call create
        OrganizationalUnit createdOu = client.create(ou);

        return createdOu;
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
