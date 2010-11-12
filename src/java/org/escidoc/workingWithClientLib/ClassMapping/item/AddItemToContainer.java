package org.escidoc.workingWithClientLib.ClassMapping.item;


import org.escidoc.Constants;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.container.Container;

/**
 * Example how to add members to a Container.
 * 
 * Precondition: A Container and the member(s) have to exist.
 * 
 * @author MRO
 * 
 */
public class AddItemToContainer {

    /**
     * 
     * @param args
     *            discarded
     */
    public static void main(String[] args) {

        try {

            // authentication (Use a user account with update permission for
            // the selected Container.
            Authentication auth =
                new Authentication(Constants.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            
            // retrieve existing Container
            // adjust objid
            ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
            
            chc.setHandle(auth.getHandle());
            Container container = chc.retrieve("escidoc:6003");
            
            // Members to add
            // adjust objids
            String[] members = { "escidoc:6001", "escidoc:6002" };
            
            // Do it
            addMember(auth, container, members);
            
            auth.logout();

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add Member to a Container.
     * 
     * <p>
     * Adding member to a Container is a task oriented method and needs and
     * additional parameter instead of the Container representation.
     * </p>
     * <p>
     * The taskParam has to contain the last-modification-date of the Container
     * (optimistic locking) and the member reference(s) comment.
     * </p>
     * <p>
     * One can add multiple member in one step.
     * </p>
     * 
     * @param objid
     *            The objid of the Container.
     * @throws EscidocClientException
     */
    public static void addMember(
        final Authentication auth, final Container container,
        final String[] members) throws EscidocClientException {

        // get handler
        ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
        
        chc.setHandle(auth.getHandle());

        // prepare taskParam and call release
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(container.getLastModificationDate());
        taskParam.setComment("Members added");

        for (int i = 0; i < members.length; i++) {
            taskParam.addResourceRef(members[i]);
        }

        Result releaseResult = chc.addMembers(container.getObjid(), taskParam);

        System.out.println("Members to Container with objid='"
            + container.getObjid() + "' added at '"
            + releaseResult.getLastModificationDate() + "'.");
    }
}