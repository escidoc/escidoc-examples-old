package org.escidoc.workingWithClientLib.ClassMapping.crud;

import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * @author SWA
 * 
 */
public class Demo1 {

    /**
     * @param args
     */
    public static void main(String[] args) {

        DemoItem c = new DemoItem();

        try {
            // authentication (Use a user account with permission on the Item).
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            c.createItem(auth);
            c.lifecycle(auth);

        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

}
