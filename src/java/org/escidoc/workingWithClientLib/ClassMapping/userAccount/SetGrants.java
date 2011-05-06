package org.escidoc.workingWithClientLib.ClassMapping.userAccount;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.GrantProperties;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.common.reference.RoleRef;

/**
 * Example how to add a grant to an existing user account.
 * 
 * @author JHE
 * 
 */
public class SetGrants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String userAccountId = "escidoc:user-account";
		String contextId = "escidoc:context-id";
		String roleObjectId = "escidoc:role-object-id";

		if (args.length == 1) {
			userAccountId = args[0];
		} else if (args.length == 2) {
			userAccountId = args[0];
			contextId = args[1];
		} else if (args.length == 3) {
			userAccountId = args[0];
			contextId = args[1];
			roleObjectId = args[2];
		}

		Grant grant = new Grant();
		GrantProperties grantProperties = new GrantProperties();
		grantProperties.setGrantRemark("new context grant");
		grantProperties.setAssignedOn(new ContextRef(contextId));
		RoleRef roleRef = new RoleRef(roleObjectId);
		grantProperties.setRole(roleRef);
		grant.setGrantProperties(grantProperties);

		try {
			grant = createGrant(userAccountId, grant);

			// write out objid and last modification date
			System.out.println("Grants set for User Account with objid='"
					+ userAccountId + "' at '"
					+ grant.getLastModificationDate() + "'.");

		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (EscidocException e) {
			e.printStackTrace();
		}
	}

	private static Grant createGrant(final String userAccountId, Grant grant)
			throws TransportException, MalformedURLException, EscidocException,
			InternalClientException {

		// prepare client object
		Authentication auth = new Authentication(new URL(
				Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN,
				Constants.USER_PASSWORD_SYSADMIN);
		UserAccountHandlerClient client = new UserAccountHandlerClient(
				auth.getServiceAddress());
		client.setHandle(auth.getHandle());

		return client.createGrant(userAccountId, grant);
	}

}
