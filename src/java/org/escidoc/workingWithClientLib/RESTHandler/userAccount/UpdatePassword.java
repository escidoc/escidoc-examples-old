package org.escidoc.workingWithClientLib.RESTHandler.userAccount;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestUserAccountHandlerClient;

/**
 * Example how to update a user password by using the XML REST representation
 * and the eSciDoc Java client library.
 * 
 * Be aware that this method will not work if the user are stored within
 * Shibboleth or LDAP. This is only for local user within the database (which is
 * not the proposed installation).
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
public class UpdatePassword {

    /**
     * Update user password (local database).
     * 
     * @param args
     *            If args[0] is given, than is it taken as objid of user,
     *            otherwise default objid is used
     */
    public static void main(String[] args) {

        String objid = "escidoc:user-account";
        String password = "mySecretPassword";

        /**
         * Update user password.
         * 
         * @param args
         *            If args[0] is given, than is it taken as objid of user
         *            account. If arg[1] is given too, than is it used as
         *            password. Otherwise default values are used.
         */
        if (args.length == 1) {
            objid = args[0];
        }
        else if (args.length == 2) {
            objid = args[0];
            password = args[1];
        }
        else if (args.length > 2) {
            System.err.println("Wrong parameter count. Using defaults.");
        }

        try {
            updatePassword(objid, password);
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the password of an user.
     * 
     * Procedure:<br/>
     * <ul/>
     * <li>get last modification date of user by retrieving this resource</li>
     * <li>call task oriented updatePassword method with new password as element
     * of task parameter</li>
     * </ul>
     * 
     * @param userId
     *            objid of user account
     * @param password
     *            New password for user
     * @throws EscidocClientException
     *             Thrown if update failed
     */
    private static void updatePassword(
        final String userId, final String password)
        throws EscidocClientException {

        RestUserAccountHandlerClient ruahc = new RestUserAccountHandlerClient();
        ruahc.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);

        String userXml = ruahc.retrieve(userId);

        // get last modification date
        String[] objidLmd = Util.obtainObjidAndLmd(userXml);

        // prepare taskParam
        final String taskParamXML =
            "<param last-modification-date=\"" + objidLmd[1] + "\" >\n"
                + "<password>" + password + "</password>\n" + "</param>";

        ruahc.updatePassword(userId, taskParamXML);
    }

}
