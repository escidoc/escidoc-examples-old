package org.escidoc.workingWithClientLib.RESTHandler.container;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
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
        throws EscidocClientException, MalformedURLException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
        RestContainerHandlerClient client = new RestContainerHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());
        
        String createdContainer = client.update(containerId, containerXml);

        return createdContainer;
    }

    private static String retrieveContainer(String containerId)
        throws EscidocClientException, MalformedURLException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
        RestContainerHandlerClient client = new RestContainerHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());
        
        String itemXml = client.retrieve(containerId);

        return itemXml;
    }

}
