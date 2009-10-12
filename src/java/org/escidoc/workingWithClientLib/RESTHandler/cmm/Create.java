package org.escidoc.workingWithClientLib.RESTHandler.cmm;

import java.io.IOException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class Create {

    /**
     * @param args
     */
    public static void main(String[] args) {

        ContentModelEx cmmEx = new ContentModelEx();

        try {
            cmmEx.create();
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
