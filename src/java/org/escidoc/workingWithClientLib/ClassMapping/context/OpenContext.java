package org.escidoc.workingWithClientLib.ClassMapping.context;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.context.Context;

/**
 * Example how to open an Context by using the eSciDoc Java client library.
 * 
 * A Context could only be used if it is in status open.
 * 
 * @author SWA
 * 
 */
public class OpenContext {

    public static void main(String[] args) {

        // set objid of Context which is to open
        String objid = "escidoc:4001";
        if (args.length > 0) {
            objid = args[0];
        }

        try {
            // authentication (Use a user account with write permission
            // on the selected Context. Usually is this user with administrator
            // role).
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            // get service handler
            ContextHandlerClient client = new ContextHandlerClient(auth.getServiceAddress());
            
            client.setHandle(auth.getHandle());

            // retrieve an already created Context
            Context context = client.retrieve(objid);

            // prepare taskParam and call open
            TaskParam taskParam = new TaskParam();
            taskParam.setComment("Example to open Context");
            taskParam
                .setLastModificationDate(context.getLastModificationDate());

            Result result = client.open(context.getObjid(), taskParam);

            // for convenient reason: print out objid and last-modification-date
            // of opened context
            System.out.println("Context with objid='" + context.getObjid()
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
