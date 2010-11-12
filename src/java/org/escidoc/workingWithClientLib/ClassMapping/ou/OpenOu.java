package org.escidoc.workingWithClientLib.ClassMapping.ou;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.oum.OrganizationalUnit;


/**
 * Example how to open an Organizational Unit by using the eSciDoc Java client library.
 * 
 * A Context could only be used if it is in status open.
 * 
 * @author SWA
 * 
 */
public class OpenOu {

    public static void main(String[] args) {

        // set objid of Organizational Unit which is to open
        String objid = "escidoc:1";
        if (args.length > 0) {
            objid = args[0];
        }

        try {
            // authentication (Use a user account with write permission
            // on the selected Organizational Unit. Usually is this user with administrator
            // role).
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // get service handler
            OrganizationalUnitHandlerClient client = new OrganizationalUnitHandlerClient(auth.getServiceAddress());
            
            client.setHandle(auth.getHandle());

            // retrieve an already created Organizational Unit
            OrganizationalUnit ou = client.retrieve(objid);

            // prepare taskParam and call open
            TaskParam taskParam = new TaskParam();
            taskParam.setComment("Example to open Organizational Unit");
            taskParam
                .setLastModificationDate(ou.getLastModificationDate());

            Result result = client.open(ou.getObjid(), taskParam);

            // for convenient reason: print out objid and last-modification-date
            // of opened Organizational Unit
            System.out.println("Organizational Unit with objid='" + ou.getObjid()
                + "' at '" + result.getLastModificationDate()
                + "' is opened now.");
            
            auth.logout();

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
    }
}
