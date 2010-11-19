package org.escidoc.workingWithClientLib.ClassMapping.ou;

import java.io.IOException;
import java.util.Iterator;

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
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.OrganizationalUnitList;
import de.escidoc.core.resources.oum.Parent;
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
     * Two Organizational Units are created. The first OU will be parent of the
     * second OU. The parent relation is set during created of the second OU.
     * 
     * A third OU is created afterward and added as parent to the second OU to
     * show the process of creating parent relations with already created
     * Organizational Units.
     */
    public static void main(String[] args) {

        try {
            // authentication (Use a user account with permission to create and
            // update an Organizational Unit).
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // Create the first Organizational Unit
            OrganizationalUnit ou1 = prepareOrganizationalUnit();
            ou1 = createOrganizationalUnit(auth, ou1);

            // for convenient reason: print out objid and last-modification-date
            // of created Organizational Unit
            System.out.println("Organizational Unit with objid='"
                + ou1.getObjid() + "' at '" + ou1.getLastModificationDate()
                + "' created.");

            // prepare the second OU
            OrganizationalUnit ou2 = prepareOrganizationalUnit();
            ou2.getParents().add(new Parent(ou1.getObjid()));

            ou2 = createOrganizationalUnit(auth, ou2);

            // for convenient reason: print out objid and last-modification-date
            // of created Organizational Unit
            System.out.println("Organizational Unit with objid='"
                + ou2.getObjid() + "' at '" + ou2.getLastModificationDate()
                + "' created.");

            /*
             * Create the third Organizational Unit.
             */
            OrganizationalUnit ou3 = prepareOrganizationalUnit();
            ou3 = createOrganizationalUnit(auth, ou3);

            // set third Organizational Unit as parent of the second
            // Organizational Unit
            OrganizationalUnitHandlerClient client =
                new OrganizationalUnitHandlerClient(auth.getServiceAddress());
            
            client.setHandle(auth.getHandle());

            ou2.getParents().add(new Parent(ou3.getObjid()));

            ou2 = client.update(ou2);

            // print parent child relations
            printOut("First", client, ou1);
            printOut("Second", client, ou2);
            printOut("Third", client, ou3);
            
            auth.logout();
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
        final Authentication auth, final OrganizationalUnit ou)
        throws EscidocException, InternalClientException, TransportException {

        // get handler
        OrganizationalUnitHandlerClient client =
            new OrganizationalUnitHandlerClient(auth.getServiceAddress());
        
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

    /**
     * Print out parents and childs of the Organizational Unit.
     * 
     * @param name
     *            The name
     * @param client
     *            The OrganizationalUnitHandlerClient
     * @param ou
     *            The OrganizationalUnit
     * @throws EscidocException
     * @throws InternalClientException
     * @throws TransportException
     */
    private static void printOut(
        final String name, final OrganizationalUnitHandlerClient client,
        final OrganizationalUnit ou) throws EscidocException,
        InternalClientException, TransportException {

        System.out.println(name + " Organizational Unit " + ou.getObjid());

        printParents(client, ou);
        printChilds(client, ou);
    }

    /**
     * Print out parents of OrganizationalUnit.
     * 
     * @param client
     *            OrganizationalUnitHandlerClient
     * @param ou
     *            The OrganizationalUnit
     * @throws EscidocException
     * @throws InternalClientException
     * @throws TransportException
     */
    private static void printParents(
        final OrganizationalUnitHandlerClient client,
        final OrganizationalUnit ou) throws EscidocException,
        InternalClientException, TransportException {

        OrganizationalUnit ouR = client.retrieve(ou.getObjid());
        System.out.println(" parents: ");
        if (ouR.getParents() == null) {
            System.out.println("   none");
        }
        else {
            Iterator<Parent> it = ouR.getParents().iterator();
            while (it.hasNext()) {
                Parent p = it.next();
                System.out.println("   " + p.getObjid());
            }
        }
    }

    /**
     * Print out childs of OrganizationalUnit.
     * 
     * @param client
     *            The OrganizationalUnitHandlerClient
     * @param ou
     *            The OrganizationalUnit
     * @throws EscidocException
     * @throws InternalClientException
     * @throws TransportException
     */
    private static void printChilds(
        final OrganizationalUnitHandlerClient client,
        final OrganizationalUnit ou) throws EscidocException,
        InternalClientException, TransportException {

        OrganizationalUnitList ouCL =
            client.retrieveChildObjects(ou.getObjid());

        System.out.println(" childs: ");
        if (ouCL.isEmpty()) {
            System.out.println("   none");
        }
        else {
            Iterator<OrganizationalUnit> it =
                ouCL.iterator();
            while (it.hasNext()) {
                OrganizationalUnit c = it.next();
                System.out.println("   " + c.getObjid());
            }
        }

    }

}
