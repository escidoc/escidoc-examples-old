package org.escidoc.workingWithClientLib.RESTHandler.item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

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
     *            If args[0] is given, than is it taken as filename for the resource template,
     *            otherwise default template is used
     *            If tree args are given, placeholders for CONTEXT_ID and CONTENT_MODEL_ID 
     *            in the resource template will be replaced
     */
    public static void main(String[] args) {

        // select template
        String xmlFile = "templates/generic/item/Item_create_minimal.xml";
        if (args.length == 1) {
            xmlFile = args[0];
        }
        
        try {
            String template = Util.getXmlFileAsString(xmlFile);
            
            if (args.length == 3) {
                template = template.replaceAll("###CONTEXT_ID###", args[1]);
                template = template.replaceAll("###CONTENT_MODEL_ID###", args[2]);
            }
            
            String createdResource = createItem(template);

            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Item with objid='" + objidLmd[0] + "' at '" + objidLmd[1] + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    /**
     * Create Item.
     * 
     * @param itemXml
     *            Item XML
     * @return XML representation of the created Item
     * @throws EscidocClientException
     * @throws MalformedURLException
     */
    private static String createItem(final String itemXml) throws EscidocClientException, MalformedURLException {

        // get REST handler
        Authentication auth =
            new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN, Constants.USER_PASSWORD_SYSADMIN);
        RestItemHandlerClient rihc = new RestItemHandlerClient(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        // create the Item
        String createdItem = rihc.create(itemXml);

        return createdItem;
    }

}
