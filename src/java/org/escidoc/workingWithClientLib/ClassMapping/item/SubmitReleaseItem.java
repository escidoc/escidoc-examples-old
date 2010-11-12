package org.escidoc.workingWithClientLib.ClassMapping.item;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.item.Item;


/**
 * Change the status of an Item from pending, to submitted, to released.
 * 
 * Precondition:
 * <ul>
 * <li>The Item has to exist and in status 'pending'.</li>
 * <li>The configured user requires permissions to change the status.</li>
 * <li>Depending on the framework configuration could a status change to
 * 'released' require the assignment of Persistent Identifiers.</li>
 * </ul>
 * 
 * @author SWA
 * 
 */
public class SubmitReleaseItem {

    /**
     * 
     * @param args
     *            The objid of the to release Item.
     */
    public static void main(String[] args) {

        try {
            // configure the objid of the Item
            String id = "escidoc:6002";
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
     * Set Item in status 'released'.
     * 
     * <p>
     * The Item life cycle provided status transitions from pending to submitted
     * and then to released. Both steps are shown in the method.
     * </p>
     * <p>
     * If releasing fail because of missing Persistent Identifier (PID) than
     * either change the release behavior configuration or assign the required
     * (PIDs) before callinng release.
     * </p>
     * <p>
     * Submitting and releasing an Item is a task oriented method and needs and
     * additional parameter instead of the Item representation.
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

        // prepare taskParam and call release
        // using result of submit method to obtain last modification date
        taskParam = new TaskParam();
        taskParam.setLastModificationDate(submitResult
            .getLastModificationDate());
        taskParam.setComment("release");

        Result releaseResult = ihc.release(item, taskParam);

        System.out.println("Item with objid='" + id + "' at '"
            + releaseResult.getLastModificationDate() + "' released.");
        
        auth.logout();
    }
}
