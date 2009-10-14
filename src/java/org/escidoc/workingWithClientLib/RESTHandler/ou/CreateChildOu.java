package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.IOException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class CreateChildOu {

    /**
     * @param args
     */
    public static void main(String[] args) {

        OrganizationalUnitEx ouEx = new OrganizationalUnitEx();

        try {
            ouEx.createChildOu();
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
