package org.escidoc.workingWithClientLib.RESTHandler.role;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestRoleHandlerClient;


/**
 * Example how to create a user account by using the XML REST representation and
 * the eSciDoc Java client library.
 * 
 * The eSciDoc Java client library is used for communication with framework.
 * Unused is mapping between Java classes and XML representations to explain the
 * XML data structure.
 * 
 * eSciDoc XML REST representation is used. Please keep that in mind, if you
 * adapt these examples for SOAP.
 * 
 * @author SWA
 * 
 */
public class CreateRole {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // select template
        String xmlFile =
            "./templates/veranstaltungskalender/role/"
                + "Role_Termin_Depositor.xml";

        if (args.length == 1) {
            xmlFile = args[0];
        }

        try {
            String createdResource = create(xmlFile);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Role with objid='" + objidLmd[0]
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
     * Create Organizational Unit (from REST XML template).
     * 
     * @param xmlTemplFile
     *            Path of the UserAccount REST XML representation template file
     * @return XML representation of the created UserAccount
     * 
     * @throws IOException
     * @throws EscidocClientException
     */
    private static String create(final String xmlTemplFile) throws IOException,
        EscidocClientException {

        // authentication (Use a user account with permission to create an
        // User Account).
        Authentication auth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get handler for user account
        RestRoleHandlerClient ruahc = new RestRoleHandlerClient(auth.getServiceAddress());
        
        ruahc.setHandle(auth.getHandle());

        // load XML template of user account
        File templ = new File(xmlTemplFile);
        String resourceXml = Util.getXmlFileAsString(templ);

        // create
        String crXML = ruahc.create(resourceXml);
        
        auth.logout();

        return crXML;
    }

}
