package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

/**
 * Example how to update an Item by using the XML REST representation and the
 * eSciDoc Java client library.
 * 
 * The eSciDoc Java client library is used for communication with framework.
 * Unused is mapping between Java classes and XML representations to explain the
 * XML data structure.
 * 
 * eSciDoc XML REST representation is used. Please keep that in mind, if you
 * adapt these examples for SOAP.
 * 
 */
public class UpdateItem {

    public static void main(String[] args) {

        String id = "escidoc:item";
        String xmlFile = "templates/generic/item/Item_update.xml";

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
            // can not load xml file, create file from item
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
        throws EscidocClientException {

        RestItemHandlerClient client = new RestItemHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);
        String createdItem = client.update(itemId, itemXml);

        return createdItem;
    }

    private static String retrieveItem(String itemId)
        throws EscidocClientException {

        RestItemHandlerClient client = new RestItemHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);
        String itemXml = client.retrieve(itemId);

        return itemXml;
    }

}
