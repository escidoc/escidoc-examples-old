package org.escidoc.workingWithClientLib.ClassMapping.userAccount;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.useraccount.UserAccountProperties;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.test.client.Constants;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Create User Accounts in the infrastructure. Shibboleth and LDAP are not
 * supported by this way. If you use Shibboleth or LDAP use methods of these
 * services to create user accounts.
 * 
 * @author SWA
 * 
 */
public class CreateUserAccount {

    /**
     * Create a User Account at the infrastructure internal user management and
     * set password.
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        try {
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.SYSTEM_ADMIN_USER,
                    Constants.SYSTEM_ADMIN_PASSWORD);

            // login
            UserAccountHandlerClient uac = new UserAccountHandlerClient();
            uac.setServiceAddress(EscidocClientTestBase.DEFAULT_SERVICE_URL);
            uac.setHandle(auth.getHandle());

            // create
            UserAccount ua = uac.create(prepareUserAccount());

            System.out.println("UserAccount with login '"
                + ua.getProperties().getLoginName() + "', " + "objid '"
                + ua.getObjid() + "', and user name '"
                + ua.getProperties().getName() + "' created.");

            // user account exists now in the internal system (no Shibboleth, no
            // LDAP)

            // set password
            final String password = "your-secret-password";

            TaskParam taskParam = new TaskParam();
            taskParam.setLastModificationDate(ua.getLastModificationDate());
            taskParam.setPassword(password);

            uac.updatePassword(ua.getObjid(), taskParam);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prepare an UserAccount object (not created at infrastructure).
     * 
     * @return UserAccount
     */
    private static UserAccount prepareUserAccount() {

        UserAccount ua = new UserAccount();

        // user properties
        UserAccountProperties properties = new UserAccountProperties();
        String login = "login" + System.currentTimeMillis();
        properties.setName("Name " + login);
        properties.setLoginName(login);

        ua.setProperties(properties);

        return ua;
    }

}
