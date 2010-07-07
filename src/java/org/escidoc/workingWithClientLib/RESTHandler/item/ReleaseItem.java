package org.escidoc.workingWithClientLib.RESTHandler.item;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to release an Item.
 * 
 * @author FRS
 * 
 */
public class ReleaseItem {

    /**
     * 
     * @param args
     *            If args[0] is given, it is used as objid for Item. Otherwise
     *            is escidoc:1 used.
     */
    public static void main(String[] args) {

        try {

            /*
             * Either set the objid of the Item by parameter or set the objid in
             * the following id variable.
             */
            String id = "escidoc:item";
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
     * (optimistic locking) and can contain a release comment.
     * </p>
     * 
     * @param id
     * @throws EscidocClientException
     */
    public static void releaseItem(final String id)
        throws EscidocClientException {

        // authentication (Use a user account with permission to release an
        // Item).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get REST handler
        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.setServiceAddress(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        // retrieving the Item
        String itemXml = rihc.retrieve(id);

        // we need last-modification-date for release
        String[] objidLmd = Util.obtainObjidAndLmd(itemXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<comment>release</comment>\n" + "</param>";

        // release
        String releaseResultXml = rihc.release(id, taskParam);

        // obtain last-modification-date for further processing
        objidLmd = Util.obtainObjidAndLmd(releaseResultXml);
        System.out.println("Item with objid='" + id + "' at '" + objidLmd[1]
            + "' released.");
    }
}
