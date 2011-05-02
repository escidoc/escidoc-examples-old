package org.escidoc.workingWithClientLib.ClassMapping.item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

public class UpdateItem {

    public static void main(String[] args) {

        String id = "escidoc:1";
        String xmlFile = "templates/TUE/Item_update.xml";

        try {

            String updatedResource =
                updateItem(id, Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(updatedResource);
            System.out.println("Item with objid='" + objidLmd[0] + "' at '"
                + objidLmd[1] + "' updated.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // can not load xml, create it
            try {

                Util.storeXmlStringAsFile(retrieveItem(id), xmlFile);

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

    private static String updateItem(String itemId, String itemXml)
        throws EscidocClientException, MalformedURLException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
        RestItemHandlerClient client = new RestItemHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());
        
        String createdItem = client.update(itemId, itemXml);

        return createdItem;
    }

    private static String retrieveItem(String itemId)
        throws EscidocClientException, MalformedURLException {
    	
    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
        RestItemHandlerClient client = new RestItemHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());
        
        String itemXml = client.retrieve(itemId);

        return itemXml;
    }

}
