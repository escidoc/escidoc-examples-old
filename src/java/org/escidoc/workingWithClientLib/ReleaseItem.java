package org.escidoc.workingWithClientLib;

import java.util.Vector;

import org.escidoc.Constants;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;

public class ReleaseItem {

    public static void main(String[] args) {
        String id = "escidoc:606";
        if (args.length > 0) {
            id = args[0];
        }
        releaseItem(id);

    }

    public static void releaseItem(String id) {

        try {

            ItemHandlerClient ihc = new ItemHandlerClient();
            ihc.login(Constants.DEFAULT_SERVICE_URL,
                Constants.SYSTEM_ADMIN_USER, Constants.SYSTEM_ADMIN_PASSWORD);

            Item item = ihc.retrieve(id);

            Result submitResult =
                ihc.submit(item, new TaskParam(item.getLastModificationDate(),
                    "submit", null, null, new Vector<Filter>()));

            Result releaseResult =
                ihc.release(item, new TaskParam(submitResult
                    .getLastModificationDate(), "release", null, null,
                    new Vector<Filter>()));

            System.out.println("Item " + id + " released.");
        }
        catch (EscidocException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (TransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (EscidocClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
