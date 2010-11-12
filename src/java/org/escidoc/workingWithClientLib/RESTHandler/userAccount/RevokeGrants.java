package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
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
public class RevokeGrants {

    /**
     * @param args
     */
    public static void main(String[] args) {

        /*
         * Either set the objid of the User Account by parameter or set the
         * objid in the following id variable.
         */
        String userAccountId = "escidoc:211125";

        // select template
        String grantId = "escidoc:213125";

        try {
            revokeGrant(userAccountId, grantId);

            System.out.println("Grants revoked for User Account with objid='"
                + userAccountId);

        }
        catch (EscidocClientException e) {
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
     * @throws IOException
     * @throws EscidocClientException
     */
    private static void revokeGrant(
        final String userAccountId, final String grantId)
        throws IOException, EscidocClientException {

        // authentication (Use a user account with permission to create a
        // Grant).
        Authentication auth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get handler for user account
        RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient(auth.getServiceAddress());
        
        ruahc.setHandle(auth.getHandle());

        // retrieving the Item
        String grantXml =ruahc.retrieveGrant(userAccountId, grantId);

        // we need last-modification-date for submit
        String[] objidLmd = Util.obtainObjidAndLmd(grantXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<revocation-remark>Some revocation remark</revocation-remark>\n" 
                + "</param>";

        // revoke
        ruahc.revokeGrant(userAccountId, grantId, taskParam);
        
        auth.logout();

    }
}
