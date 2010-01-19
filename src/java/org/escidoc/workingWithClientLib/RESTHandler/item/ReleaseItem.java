package org.escidoc.workingWithClientLib.RESTHandler.item;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

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
    public static void releaseItem(String id) throws EscidocClientException {

        // prepare client object
        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // retrieving the Item
        String itemXml = rihc.retrieve(id);

        // we need last-modification-date for release
        String[] objidLmd = Util.obtainObjidAndLmd(itemXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "</param>";

        // release 
        String releaseResultXml = rihc.release(itemXml, taskParam);

        // obtain last-modification-date for further processing
        objidLmd = Util.obtainObjidAndLmd(releaseResultXml);
        System.out.println("Item with objid='" + id + "' at '" + objidLmd[1]
            + "' released.");
    }
}
