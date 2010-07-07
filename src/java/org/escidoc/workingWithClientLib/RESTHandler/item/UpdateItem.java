package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;
import de.escidoc.core.test.client.EscidocClientTestBase;

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

    /**
     * 
     * @param args
     */
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

    /**
     * Update Item.
     * 
     * @param itemId
     * @param itemXml
     * @return
     * @throws EscidocClientException
     */
    private static String updateItem(String itemId, String itemXml)
        throws EscidocClientException {

        // authentication (Use a user account with permission to submit an
        // Item).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get REST handler
        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.setServiceAddress(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        String createdItem = rihc.update(itemId, itemXml);

        return createdItem;
    }

    /**
     * Retrieve Item.
     * 
     * @param itemId
     * @return
     * @throws EscidocClientException
     */
    private static String retrieveItem(String itemId)
        throws EscidocClientException {

        // authentication (Use a user account with permission to submit an
        // Item).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get REST handler
        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.setServiceAddress(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        String itemXml = rihc.retrieve(itemId);

        return itemXml;
    }

}
