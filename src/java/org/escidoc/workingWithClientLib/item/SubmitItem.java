package org.escidoc.workingWithClientLib.item;

import java.util.Vector;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;

public class SubmitItem {

    public static void main(String[] args) {

        try {

            String id = "escidoc:1";
            if (args.length > 0) {
                id = args[0];
            }

            releaseItem(id);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    public static void releaseItem(String id) throws EscidocClientException {

        // prepare client object
        ItemHandlerClient ihc = new ItemHandlerClient();
        ihc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // create item object retrieving the item
        Item item = ihc.retrieve(id);

        // submit
        Result submitResult =
            ihc.submit(item, new TaskParam(item.getLastModificationDate(),
                "submit", null, null, new Vector<Filter>()));

        System.out.println("Item with objid='" + id + "' at '"
            + submitResult.getLastModificationDateAsString() + "' submitted.");
    }
}
