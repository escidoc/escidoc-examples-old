package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestUserAccountHandlerClient;

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
public class CreateUserAccount {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // select template
        String xmlFile =
            "./templates/generic/user-account/"
                + "escidoc_useraccount_for_create.xml";

        if (args.length == 1) {
            xmlFile = args[0];
        }

        try {
            String createdResource = create(xmlFile);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("User Account with objid='" + objidLmd[0]
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
     * Create Organizational Unit (from REST XML template).
     * 
     * @param xmlTemplFile
     *            Path of the UserAccount REST XML representation template file
     * @return XML representation of the created UserAccount
     * 
     * @throws InternalClientException
     * @throws EscidocException
     * @throws TransportException
     * @throws IOException
     */
    private static String create(final String xmlTemplFile)
        throws InternalClientException, EscidocException, TransportException,
        IOException {

        // get handler for user account
        RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient();

        // authenticate
        ruahc.login(Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // load XML template of user account
        File templ = new File(xmlTemplFile);
        String resourceXml = Util.getXmlFileAsString(templ);
        
        // create
        String crXML = ruahc.create(resourceXml);

        return crXML;
    }

}
