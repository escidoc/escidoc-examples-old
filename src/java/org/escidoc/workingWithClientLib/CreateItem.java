package org.escidoc.workingWithClientLib;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

public class CreateItem {

    public static void main(String[] args) {

        try {

            String xmlFile = "templates/TUE/Item_create_schwäbischer-dialekt.xml";
            if (args.length > 0) {
                xmlFile = args[0];
            }

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

    private static String createItem(String itemXml)
        throws EscidocClientException {

        RestItemHandlerClient client = new RestItemHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);
        String createdItem = client.create(itemXml);

        return createdItem;
    }

}
