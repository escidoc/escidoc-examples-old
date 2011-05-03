package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;

public class SubmitReleaseItem {

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
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}

    }

    public static void releaseItem(String id) throws EscidocClientException, MalformedURLException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_DEPOSITOR, Constants.USER_PASSWORD_DEPOSITOR);
    	ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
    	ihc.setHandle(auth.getHandle());

        // create item object retrieving the item
        Item item = ihc.retrieve(id);

        // submit
        TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(item.getLastModificationDate());
		taskParam.setComment("submit");
		taskParam.setFilters(new Vector<Filter>());
        Result submitResult = ihc.submit(item, taskParam);
        
        System.out.println("Item with objid='" + id + "' at '"
            + submitResult.getLastModificationDate() + "' submitted.");

        // release using submit result
        taskParam = new TaskParam();
		taskParam.setLastModificationDate(item.getLastModificationDate());
		taskParam.setComment("release");
		taskParam.setFilters(new Vector<Filter>());
        
        Result releaseResult = ihc.release(item, taskParam);

        System.out.println("Item with objid='" + id + "' at '"
            + releaseResult.getLastModificationDate() + "' released.");
    }
}
