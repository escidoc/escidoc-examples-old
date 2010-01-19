package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestOrganizationalUnitHandlerClient;

/**
 * Example how to create an Organizational Unit and simultaneously defining it
 * as child of another Organizational Unit.
 * 
 * @author SWA
 * 
 */
public class CreateChildOu {

    /**
     * @param args
     *            args are ignored
     */
    public static void main(String[] args) {

        // select template
        String xmlFile =
            "./templates/generic/organizational-unit/"
                + "escidoc_child_ou_for_create.xml";
        
        String parentOU = "escidoc:1";
        String ouName = "Example OU";
        String ouAltName = "Alternative Name of OU";
        String ouDescription = "This is the description of the Example OU";
        
        if (args.length > 0) {
            xmlFile = args[0];
        }

        try {
            /*
             * Alter the later used template of Organizational Unit to specify
             * the values of the to create Organizational Unit and of the
             * reference the parent Organizational Unit.
             * 
             * The referenced parent Organizational Unit has to exist.
             */
            // load XML template of organizational unit
            File templOu = new File(xmlFile);
            String ouXml = Util.getXmlFileAsString(templOu);

            // replace placeholder in template
            ouXml = ouXml.replace("###PARENT_OU_ID###", parentOU);
            ouXml = ouXml.replace("###NAME_OF_OU###", ouName);
            ouXml = ouXml.replace("###ALTERNATIVE_NAME_OF_OU###", ouAltName);
            ouXml = ouXml.replace("###DESCRIPTION_OF_OU###", ouDescription);

            String createdResource = createChildOu(ouXml);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Organizational Unit with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' created");
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
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create Organizational Unit (from REST XML template) as child of other OU.
     * 
     * @throws InternalClientException
     * @throws EscidocException
     * @throws TransportException
     * @throws IOException
     */
    private static String createChildOu(final String ouXml)
        throws InternalClientException, EscidocException, TransportException,
        IOException {

        // get handler for organizational units
        RestOrganizationalUnitHandlerClient rouc =
            new RestOrganizationalUnitHandlerClient();

        // authenticate
        rouc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // create
        String crtdOuXML = rouc.create(ouXml);

        return crtdOuXML;
    }

}
