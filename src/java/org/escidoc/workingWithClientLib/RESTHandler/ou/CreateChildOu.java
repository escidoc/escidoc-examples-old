package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
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

            String createdResource = createChildOu(ouXml);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Organizational Unit with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' created");
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create Organizational Unit (from REST XML template) as child of other OU.
     * 
     * @param ouXml
     *            Organizational Unit representation for create
     * @throws IOException
     * @throws EscidocClientException
     */
    private static String createChildOu(final String ouXml) throws IOException,
        EscidocClientException {

        // authentication (Use a user account with permission to create an
        // Organizational Unit).
        Authentication auth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get rest handler
        RestOrganizationalUnitHandlerClient rouc =
            new RestOrganizationalUnitHandlerClient(auth.getServiceAddress());
        // login
        
        rouc.setHandle(auth.getHandle());

        // create
        String crtdOuXML = rouc.create(ouXml);
        
        auth.logout();

        return crtdOuXML;
    }

}
