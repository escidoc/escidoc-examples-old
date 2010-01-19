package org.escidoc.workingWithClientLib.RESTHandler.container;

import java.util.Vector;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.container.Container;

public class ReleaseContainer {

    public static void main(String[] args) {

        String id = "escidoc:1";
        if (args.length > 0) {
            id = args[0];
        }

        try {

            releaseContainer(id);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    public static void releaseContainer(String id)
        throws EscidocClientException {

        // prepare client object
        ContainerHandlerClient chc = new ContainerHandlerClient();
        chc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // create Container object retrieving the container
        Container container = chc.retrieve(id);

        // submit
        Result submitResult =
            chc.submit(container, new TaskParam(container
                .getLastModificationDate(), "submit", null, null,
                new Vector<Filter>()));

        // release using submit result
        Result releaseResult =
            chc.release(container, new TaskParam(submitResult
                .getLastModificationDate(), "release", null, null,
                new Vector<Filter>()));

        System.out.println("Container with objid='" + id + "' at '"
            + releaseResult.getLastModificationDateAsString() + "' released.");
    }
}
