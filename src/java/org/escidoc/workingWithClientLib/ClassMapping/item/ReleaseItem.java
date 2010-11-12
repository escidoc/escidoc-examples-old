package org.escidoc.workingWithClientLib.ClassMapping.item;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;


/**
 * Example how to release an Item.
 * 
  * <ul>
 * <li>The Item has to exist and in status 'submitted'.</li>
 * <li>The configured user requires permissions to change the status.</li>
 * </ul>
 * 
 * @author FRS
 */
public class ReleaseItem {

    /**
     * 
     * @param args
     *            The objid of the to release Item.
     */
    public static void main(String[] args) {

        try {

            /*
             * Either set the objid of the Item by parameter or set the objid in
             * the following id variable.
             */
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
     * Release an Item.
     * 
     * <p>
     * Release an Item is a task oriented method and needs and additional
     * parameter instead of the Item representation.
     * </p>
     * <p>
     * The taskParam has to contain the last-modification-date of the Item
     * (optimistic locking) and the release comment.
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

        // prepare taskParam and call release
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(item.getLastModificationDate());
        taskParam.setComment("release");

        Result releaseResult = ihc.release(item, taskParam);

        System.out.println("Item with objid='" + id + "' at '"
            + releaseResult.getLastModificationDate() + "' released.");
        
        auth.logout();
    }
}
