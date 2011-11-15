package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.ExplainRequestType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.escidoc.Constants;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.explain.Explain;
import de.escidoc.core.resources.sb.explain.ExplainResponse;
import de.escidoc.core.resources.sb.explain.Index;

/**
 * Example for calling Item filter explain.
 * 
 * @author JHE
 * 
 */
public class RequestExplain {

    /**
     * @param args
     */
    public static void main(String[] args) {

        URL serviceAddress = null;
        try {
            serviceAddress = new URL(Constants.DEFAULT_SERVICE_URL);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

    	ItemHandlerClientInterface c = new ItemHandlerClient(serviceAddress);

        ExplainRequestType request = new ExplainRequestType();

        ExplainResponse response = null;

        try {
            response = c.retrieveItems(request);
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

        Record<Explain> record = response.getRecord();
        Explain data = record.getRecordData();

        List<Index> indexes = data.getIndexInfo().getIndexes();

        System.out.println("=======================================");
        System.out.println("RecordPacking: " + RecordPacking.XML.toString());
        System.out.println("available search fields for Items:");
        System.out.println("=======================================");
        for (final Index index : indexes) {
            System.out.println(index.getTitle());
        }
        System.out.println("\n");

    }

}
