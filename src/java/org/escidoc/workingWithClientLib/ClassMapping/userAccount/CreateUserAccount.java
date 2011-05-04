package org.escidoc.workingWithClientLib.ClassMapping.userAccount;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.joda.time.DateTime;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.useraccount.UserAccountProperties;

/**
 * Example how to create a user account by using the eSciDoc Java client library.
 * 
 * @author JHE
 * 
 */
public class CreateUserAccount {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Prepare a value object with new values of Organizational Unit
        try {

            // Prepare a value object with new values of User Account
            UserAccount userAccount = prepareUserAccount();

            userAccount = createUserAccount(userAccount);
            
            
            // for convenient reason: print out objid and last-modification-date
            // of created User Account
            System.out.println("User Account with objid='"
                + userAccount.getObjid() + "' at '" + userAccount.getLastModificationDate()
                + "' created.");

        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (EscidocException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static UserAccount createUserAccount(UserAccount userAccount) throws InternalClientException,
        TransportException, MalformedURLException, EscidocException {

        Authentication auth =
            new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN,
                Constants.USER_PASSWORD_SYSADMIN);
        UserAccountHandlerClient client = new UserAccountHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        client.create(userAccount);

        return userAccount;
    }

    private static UserAccount prepareUserAccount() {

        UserAccount userAccount = new UserAccount();

        UserAccountProperties properties = new UserAccountProperties();
        properties.setLoginName("my_account");
        properties.setCreationDate(new DateTime());
        properties.setName("Max Mustermann");
        
        userAccount.setProperties(properties);

        return userAccount;
    }

}
