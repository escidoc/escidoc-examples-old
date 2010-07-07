package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Item.
 * 
 * @author FRS
 * 
 */
public class CreateItem {

    /**
     * Create Item.
     * 
     * @param args
     *            If args[0] is given, than is it taken as filename for the
     *            resource template, otherwise default template is used
     */
    public static void main(String[] args) {

        // select template
        String xmlFile = "templates/generic/item/Item_create_minimal.xml";
        if (args.length > 0) {
            xmlFile = args[0];
        }

        try {
            String createdResource =
                createItem(Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Item with objid='" + objidLmd[0] + "' at '"
                + objidLmd[1] + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    /**
     * Create Item.
     * 
     * @param itemXml
     *            Item XML
     * @return XML representation of the created Item
     * @throws EscidocClientException
     */
    private static String createItem(final String itemXml)
        throws EscidocClientException {

        // authentication (Use a user account with permission to create an Item
        // - usually the depositor role).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get REST handler
        RestItemHandlerClient rihc = new RestItemHandlerClient();
        rihc.setServiceAddress(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        // create the Item
        String createdItem = rihc.create(itemXml);

        return createdItem;
    }

}
