package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.ExplainRequestType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.escidoc.Constants;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.explain.Explain;
import de.escidoc.core.resources.sb.explain.ExplainResponse;
import de.escidoc.core.resources.sb.explain.Index;

/**
 * Example for SRW Search.
 * 
 * @author JHE
 * 
 */
public class SRWExplain {

    private final static String SEARCH_INDEX = "escidoc_all";

    /**
     * @param args
     *            ignored
     */
    public static void main(String[] args) {

        URL serviceAddress = null;
        try {
            serviceAddress = new URL(Constants.DEFAULT_SERVICE_URL + "/srw/search/" + SEARCH_INDEX);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ExplainRequestType request = new ExplainRequestType();

        SearchHandlerClient c = new SearchHandlerClient(serviceAddress);

        ExplainResponse response = null;
        try {
            response = c.explain(request, null);
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

        Record<Explain> record = response.getRecord();
        Explain data = record.getRecordData();

        List<Index> indexes = data.getIndexInfo().getIndexes();

        System.out.println("======================================");
        System.out.println("Example for SRW Explain: ");
        System.out.println("RecordPacking: " + RecordPacking.XML.toString());
        System.out.println("available search fields for Database '" + SEARCH_INDEX + "':");
        System.out.println("======================================");

        for (Index index : indexes) {
            System.out.println(index.getTitle());
        }
    }
}
