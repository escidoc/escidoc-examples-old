package org.escidoc.workingWithClientLib.ClassMapping.ou;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.test.client.EscidocClientTestBase;

public class ChangeOuState {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// set objid of Context which is to open
        String objid = "escidoc:4";
        if (args.length > 0) {
            objid = args[0];
        }
		
		try {
            // authentication (Use a user account with permission to create an
            // Organizational Unit).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // get handler
            OrganizationalUnitHandlerClient client =
                new OrganizationalUnitHandlerClient();
            client.setServiceAddress(auth.getServiceAddress());
            client.setHandle(auth.getHandle());

            // call create
            OrganizationalUnit createdOu = client.retrieve(objid);

            // prepare taskParam and call open
            TaskParam taskParam = new TaskParam();
            taskParam.setComment("Example to change OU state");
            taskParam.setLastModificationDate(createdOu.getLastModificationDate());
            taskParam.setComment("open");

            Result result = client.open(createdOu.getObjid(), taskParam);

            // for convenient reason: print out objid and last-modification-date
            // of opened context
            System.out.println("Context with objid='" + createdOu.getObjid()
                + "' at '" + result.getLastModificationDate()
                + "' is opened now.");
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
	}

}
