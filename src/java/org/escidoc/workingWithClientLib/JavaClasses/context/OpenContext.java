package org.escidoc.workingWithClientLib.JavaClasses.context;

import java.util.Vector;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.context.Context;

public class OpenContext {

    public static void main(String[] args) {

        try {

            String id = "escidoc:1";
            if (args.length > 0) {
                id = args[0];
            }

            openContext(id);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    public static void openContext(String id) throws EscidocClientException {

        // prepare client object
        ContextHandlerClient chc = new ContextHandlerClient();
        chc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // create Context object retrieving the context
        Context context = chc.retrieve(id);

        // submit
        Result openResult =
            chc.open(context.getObjid(), new TaskParam(context.getLastModificationDate(),
                "open", null, null, new Vector<Filter>()));

        System.out.println("Context with objid='" + id + "' at '"
            + openResult.getLastModificationDateAsString() + "' opened.");
    }
}
