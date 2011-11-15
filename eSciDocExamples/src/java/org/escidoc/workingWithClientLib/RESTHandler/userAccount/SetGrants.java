package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestUserAccountHandlerClient;

/**
 * Example how to add grants to an existing user account by using the XML REST
 * representation and the eSciDoc Java client library.
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
public class SetGrants {

    /**
     * @param args
     */
    public static void main(String[] args) {

        /*
         * Either set the objid of the User Account by parameter or set the
         * objid in the following id variable.
         */
        String userAccountId = "escidoc:user-account";

        // select template
        String xmlFile =
            "./templates/generic/user-account/"
                + "escidoc_depositor_grant_for_create.xml";
        
        if (args.length == 1) {
            userAccountId = args[0];
        }
        
        if (args.length == 2) {
            userAccountId = args[0];
            xmlFile = args[1];
        }

        try {
            String resourceXml =
                createGrant(userAccountId, xmlFile);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(resourceXml);
            System.out.println("Grants set for User Account with objid='"
                + userAccountId + "' at '" + objidLmd[1] + "'.");

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
     * Set grants for user.
     * 
     * @param userAccountId
     *            The objid of the User Account.
     * @param contextObjid
     *            The objid of the Context where the grants are set
     * @param xmlTemplFile
     *            Path of the Grant REST XML representation template file
     * @return XML representation of the created grant
     * 
     * @throws InternalClientException
     * @throws EscidocException
     * @throws TransportException
     * @throws IOException
     */
    private static String createGrant(
        final String userAccountId, 
        final String xmlTemplFile) throws InternalClientException,
        EscidocException, TransportException, IOException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
        RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient(auth.getServiceAddress());
        ruahc.setHandle(auth.getHandle());

        // load and prepare the XML for grants
        File templ = new File(xmlTemplFile);
        String grantXml = Util.getXmlFileAsString(templ);

        // create
        String crXML = ruahc.createGrant(userAccountId, grantXml);

        return crXML;
    }
}
