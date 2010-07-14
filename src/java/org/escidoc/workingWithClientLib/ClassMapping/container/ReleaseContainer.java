package org.escidoc.workingWithClientLib.ClassMapping.container;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to release a Container.
 * 
 * @author SWA
 * 
 */
public class ReleaseContainer {

    /**
     * 
     * @param args
     *            The objid of the to release Container.
     */
    public static void main(String[] args) {

        try {

            /*
             * Either set the objid of the Container by parameter or set the
             * objid in the following id variable.
             */
            String id = "escidoc:1";
            if (args.length > 0) {
                id = args[0];
            }

            // authentication (Use a user account with write permission for
            // Container
            // on the selected Context. Usually is this user with depositor
            // role).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            releaseContainer(auth, id);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * Release a Container.
     * 
     * <p>
     * Release an Container is a task oriented method and needs and additional
     * parameter instead of the Container representation.
     * </p>
     * <p>
     * The taskParam has to contain the last-modification-date of the Container
     * (optimistic locking) and the release comment.
     * </p>
     * 
     * @param objid
     *            The objid of the Container.
     * @throws EscidocClientException
     */
    public static void releaseContainer(final Authentication auth, final String objid)
			throws EscidocClientException {

		// get handler
		ContainerHandlerClient chc = new ContainerHandlerClient();
        chc.setServiceAddress(auth.getServiceAddress());
        chc.setHandle(auth.getHandle());

		// retrieve the Container to detect last modification date
		Container container = chc.retrieve(objid);

		// prepare taskParam and call release
		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(container
            .getLastModificationDate());
		taskParam.setComment("Comment for release");
		
		Result releaseResult = chc.release(container, taskParam);

		System.out.println("Container with objid='" + objid + "' at '"
				+ releaseResult.getLastModificationDate()
				+ "' released.");
	}
}
