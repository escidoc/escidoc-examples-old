package org.escidoc.workingWithClientLib.RESTHandler.container;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;


/**
 * Example to sumbit and release a Container via REST interface.
 * 
 * @author FRS, SWA
 * 
 */
public class SubmitReleaseContainer {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        String id = "escidoc:container";
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

    /**
     * Submit and release Container.
     * 
     * @param id
     *            objid of Container
     * @throws EscidocClientException
     */
    public static void releaseContainer(String id)
        throws EscidocClientException {

        // authentication (Use a user account with permission to submit and
        // release a Container).
        Authentication auth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        RestContainerHandlerClient rchc = new RestContainerHandlerClient(auth.getServiceAddress());
        
        rchc.setHandle(auth.getHandle());

        // retrieving the Container
        String containerXml = rchc.retrieve(id);

        // we need last-modification-date for submit
        String[] objidLmd = Util.obtainObjidAndLmd(containerXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<comment>submit</comment>\n" + "</param>";

        // submit
        String submitResult = rchc.submit(id, taskParam);

        // obtain last-modification-date for further processing
        objidLmd = Util.obtainObjidAndLmd(submitResult);
        System.out.println("Container with objid='" + id + "' at '"
            + objidLmd[1] + "' submitted.");

        taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<comment>release</comment>\n" + "</param>";

        // release
        String releaseResultXml = rchc.release(id, taskParam);

        // obtain last-modification-date for further processing
        objidLmd = Util.obtainObjidAndLmd(releaseResultXml);
        System.out.println("Container with objid='" + id + "' at '"
            + objidLmd[1] + "' released.");
        
        auth.logout();
    }
}
