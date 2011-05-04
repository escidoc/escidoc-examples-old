package org.escidoc.workingWithClientLib.ClassMapping.userAccount;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.TaskParam;

/**
 * Example how to set a new password for an existing user account.
 * 
 * @author JHE
 * 
 */
public class UpdatePassword {

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {

            String userAccountId = "escidoc:user-account";
            String password = "escidoc:user-password";

            if (args.length == 1) {
                userAccountId = args[0];
            }

            if (args.length == 2) {
                password = args[1];
            }

            updatePassword(userAccountId, password);

            System.out.println("New password set for User Account with objid='" + userAccountId + "'");

        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * set user password
     * 
     * @param userAccountId
     * @param password
     * @throws TransportException
     * @throws MalformedURLException
     * @throws EscidocException
     * @throws InternalClientException
     */
    private static void updatePassword(final String userAccountId, String password) throws TransportException,
        MalformedURLException, EscidocException, InternalClientException {

        // prepare client object
        Authentication auth =
            new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN,
                Constants.USER_PASSWORD_SYSADMIN);
        UserAccountHandlerClient client = new UserAccountHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        TaskParam taskParam = new TaskParam();
        taskParam.setPassword(password);

        client.updatePassword(userAccountId, taskParam);
    }

}
