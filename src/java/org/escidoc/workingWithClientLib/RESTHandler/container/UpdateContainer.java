package org.escidoc.workingWithClientLib.RESTHandler.container;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;

public class UpdateContainer {

    public static void main(String[] args) {

        String id = "escidoc:container";
        String xmlFile = "templates/generic/container/Container_update.xml";

        try {

            String updatedResource =
                updateContainer(id, Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(updatedResource);
            System.out.println("Container with objid='" + objidLmd[0] + "' at '"
                + objidLmd[1] + "' updated.");

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

    private static String updateContainer(String containerId, String containerXml)
        throws EscidocClientException {

        RestContainerHandlerClient client = new RestContainerHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);
        String createdContainer = client.update(containerId, containerXml);

        return createdContainer;
    }

    private static String retrieveContainer(String containerId)
        throws EscidocClientException {

        RestContainerHandlerClient client = new RestContainerHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);
        String itemXml = client.retrieve(containerId);

        return itemXml;
    }

}
