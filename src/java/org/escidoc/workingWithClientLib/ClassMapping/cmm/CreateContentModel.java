package org.escidoc.workingWithClientLib.ClassMapping.cmm;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContentModelHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.cmm.ContentModel;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Content Model.
 * 
 * @author SWA
 * 
 */
public class CreateContentModel {

    /**
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        try {
            // authentication (Use a user account with permission to create a
            // Content Model).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            ContentModelHandlerClient cc = new ContentModelHandlerClient();
            cc.setServiceAddress(auth.getServiceAddress());
            cc.setHandle(auth.getHandle());

            ContentModel cmm = new ContentModel();
            cmm.getProperties().setName("Name-" + System.nanoTime());
            cmm.getProperties().setDescription(
                "Description-" + System.nanoTime());

            cmm = cc.create(cmm);

            System.out.println("ContentModel with objid='" + cmm.getObjid()
                + "' at '" + cmm.getLastModificationDate() + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
    }

}
