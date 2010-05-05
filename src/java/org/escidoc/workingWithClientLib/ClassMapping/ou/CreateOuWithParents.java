package org.escidoc.workingWithClientLib.ClassMapping.ou;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Properties;

/**
 * Example how to create a Organizational Unit parent relation by using the
 * class mapping feature of eSciDoc Java client library.
 * 
 * @author SWA
 * 
 */
public class CreateOuWithParents {

    /*
     * Two Organizational Units are created. The first OU is set as parent of
     * the second OU. The parent relation set with the created of the second OU
     * (It is also possible to create the parent relation after both
     * Organizational Units are created, what is explain in another example.)
     */
    public static void main(String[] args) {

        try {
            // Create the first Organizational Unit
            OrganizationalUnit ou1 = prepareOrganizationalUnit();
            ou1 = createOrganizationalUnit(ou1);

            // for convenient reason: print out objid and last-modification-date
            // of created Organizational Unit
            System.out.println("Organizational Unit with objid='"
                + ou1.getObjid() + "' at '" + ou1.getLastModificationDate()
                + "' created.");

            // prepare the second OU
            OrganizationalUnit ou2 = prepareOrganizationalUnit();
            ou2.getParents().addParentRef(new ResourceRef(ou1.getObjid()));

            ou2 = createOrganizationalUnit(ou2);

            // for convenient reason: print out objid and last-modification-date
            // of created Organizational Unit
            System.out.println("Organizational Unit with objid='"
                + ou2.getObjid() + "' at '" + ou2.getLastModificationDate()
                + "' created.");

        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
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
        properties.setName("Organizational_Unit_Test_Name");
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

        // add parent OU
        Parents parents = new Parents();
        ResourceRef resourceRef = new ResourceRef();

        resourceRef.setObjid("escidoc:ex3");
        parents.addParentRef(resourceRef);
        ou.setParents(parents);

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
     * 
     * @throws EscidocException
     *             Thrown if eSciDoc infrastructure throws an exception. This
     *             happens mostly if data structure is incomplete for the
     *             required operation, method is not allowed in object status or
     *             permissions are restricted.
     * @throws InternalClientException
     *             These are thrown if client library internal failure occur.
     * @throws TransportException
     *             Is thrown if transport between client library and framework
     *             is malfunctioned.
     */
    private static OrganizationalUnit createOrganizationalUnit(
        final OrganizationalUnit ou) throws EscidocException,
        InternalClientException, TransportException {

        // get handler
        OrganizationalUnitHandlerClient client =
            new OrganizationalUnitHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);

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
