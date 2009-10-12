package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.IOException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class Create {

    /**
     * @param args
     */
    public static void main(String[] args) {

        OrganizationalUnitEx ouEx = new OrganizationalUnitEx();

        try {
            ouEx.create();
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
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
