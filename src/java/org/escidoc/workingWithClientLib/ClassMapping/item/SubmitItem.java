package org.escidoc.workingWithClientLib.ClassMapping.item;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;


/**
 * Change public-status of an Item to 'submitted'.
 * 
 * Precondition:
 * <ul>
 * <li>The Item has to exist and in status 'pending'.</li>
 * <li>The configured user requires permissions to change the status.</li>
 * </ul>
 * 
 * @author SWA
 * 
 */
public class SubmitItem {

    /**
     * 
     * @param args
     *            The objid of the to submit Item.
     */
    public static void main(String[] args) {

        try {
            // configure the objid of the Item
            String id = "escidoc:4003";
            if (args.length > 0) {
                id = args[0];
            }

            releaseItem(id);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * Submit an Item.
     * 
     * <p>
     * Submitting an Item is a task oriented method and needs and additional
     * parameter instead of the Item representation.
     * </p>
     * <p>
     * The taskParam has to contain the last-modification-date of the Item
     * (optimistic locking) and the submit comment.
     * </p>
     * <p>
     * The Item has to be in status pending.
     * </p>
     * 
     * @param id
     *            The objid of the Item.
     * @throws EscidocClientException
     */
    public static void releaseItem(final String id)
        throws EscidocClientException {

        Authentication auth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
        
        ihc.setHandle(auth.getHandle());

        // retrieving the Item
        Item item = ihc.retrieve(id);

        // prepare TaskParam and call submit
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(item.getLastModificationDate());
        taskParam.setComment("submit");

        Result submitResult = ihc.submit(item, taskParam);

        System.out.println("Item with objid='" + id + "' at '"
            + submitResult.getLastModificationDate() + "' submitted.");
        
        auth.logout();
    }
}
