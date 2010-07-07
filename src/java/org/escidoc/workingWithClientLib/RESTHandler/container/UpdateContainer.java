package org.escidoc.workingWithClientLib.RESTHandler.container;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example to update a Container via REST interface.
 * 
 * @author FRS, SWA
 * 
 */

public class UpdateContainer {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        String id = "escidoc:container";
        String xmlFile = "templates/generic/container/Container_update.xml";

        try {

            String updatedResource =
                updateContainer(id, Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(updatedResource);
            System.out.println("Container with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' updated.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // can not load xml, create it
            try {

                Util.storeXmlStringAsFile(retrieveContainer(id), xmlFile);

            }
            catch (EscidocClientException ee) {
                ee.printStackTrace();
            }
            catch (IOException ee) {
                System.out.println("Can neither read nor write " + xmlFile
                    + ".");
            }
        }
    }

    /**
     * 
     * @param containerId
     * @param containerXml
     * @return
     * @throws EscidocClientException
     */
    private static String updateContainer(
        String containerId, String containerXml) throws EscidocClientException {

        // authentication (Use a user account with permission to update a
        // Container - usually the depositor role).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        RestContainerHandlerClient client = new RestContainerHandlerClient();
        client.setServiceAddress(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        String createdContainer = client.update(containerId, containerXml);

        return createdContainer;
    }

    /**
     * 
     * @param containerId
     * @return
     * @throws EscidocClientException
     */
    private static String retrieveContainer(String containerId)
        throws EscidocClientException {

        // authentication (Use a user account with permission to retrieve a
        // Container - usually the depositor role).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        RestContainerHandlerClient client = new RestContainerHandlerClient();
        client.setServiceAddress(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        String itemXml = client.retrieve(containerId);

        return itemXml;
    }

}
