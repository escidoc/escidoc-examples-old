package org.escidoc.workingWithClientLib.ClassMapping.ou;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.OrganizationalUnitRef;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.OrganizationalUnitProperties;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

/**
 * Example how to create an Organizational Unit by using the eSciDoc Java client
 * library.
 * 
 * @author SWA
 * 
 */
public class CreateOu {

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

        OrganizationalUnitProperties properties = new OrganizationalUnitProperties();
        properties.setName("Organizational_Unit_Test_Name");
        ou.setProperties(properties);

        /*
         * An Organizational Unit requires title, alternative title(s) and
         * description. These values are set via metadata-record with name
         * 'escidoc'.
         */
        MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName("escidoc");

        String str =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ou:organization-details "
                + "xmlns:ou=\"http://escidoc.mpg.de/metadataprofile/schema/0.1/organization\">\n"
                + "<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
                + "Generic Organizational Unit</dc:title>\n"
                + "</ou:organization-details>";
        InputStream in = new ByteArrayInputStream(str.getBytes());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        mdRecord.setContent(doc.getDocumentElement());

        // add mdRecord to set
        MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(mdRecord);

        // add metadata-records to OU
        ou.setMetadataRecords(mdRecords);

        // add parent OU
        Parents parents = new Parents();
        OrganizationalUnitRef resourceRef = new OrganizationalUnitRef("escidoc:ex3");
        parents.add(new Parent(resourceRef.getObjid()));
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
     * @throws MalformedURLException 
     */
    private static OrganizationalUnit createOrganizationalUnit(
        final OrganizationalUnit ou) throws EscidocException,
        InternalClientException, TransportException, MalformedURLException {

        // get handler
    	Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
    	OrganizationalUnitHandlerClient client = new OrganizationalUnitHandlerClient(auth.getServiceAddress());
    	client.setHandle(auth.getHandle());

        // call create
        OrganizationalUnit createdOu = client.create(ou);

        return createdOu;
    }
}
