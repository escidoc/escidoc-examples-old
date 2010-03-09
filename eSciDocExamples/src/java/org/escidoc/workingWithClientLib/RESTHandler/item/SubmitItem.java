package org.escidoc.workingWithClientLib.RESTHandler.item;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

/**
 * Example how to submit an Item.
 * 
 * @author FRS
 * 
 */
public class SubmitItem {

    /**
     * 
     * @param args
     *            The objid of the to submit Item.
     */
    public static void main(String[] args) {

        String id = "escidoc:item";
        if (args.length > 0) {
            id = args[0];
        }

        try {
            submitItem(id);
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param id
     * @throws EscidocClientException
     */
    public static void submitItem(final String id)
        throws EscidocClientException {

        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // retrieving the Item
        String itemXml = rihc.retrieve(id);

        // we need last-modification-date for release
        String[] objidLmd = Util.obtainObjidAndLmd(itemXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<comment>submit</comment>\n" + "</param>";

        // prepare taskParam and call submit
        String submitResult = rihc.submit(id, taskParam);

        // obtain last-modification-date for further processing
        objidLmd = Util.obtainObjidAndLmd(submitResult);
        System.out.println("Item with objid='" + id + "' at '" + objidLmd[1]
            + "' submitted.");
    }
}