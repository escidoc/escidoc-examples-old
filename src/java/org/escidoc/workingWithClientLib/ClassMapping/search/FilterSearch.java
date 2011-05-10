package org.escidoc.workingWithClientLib.ClassMapping.search;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.search.SearchResult;
import de.escidoc.core.resources.sb.search.SearchResultRecord;
import de.escidoc.core.resources.sb.search.SearchRetrieveResponse;

/**
 * Example for SRW filtered Search.
 * 
 * @author JHE
 * 
 */
public class FilterSearch {

    /**
     * @param args
     *            Search Query
     */
    public static void main(String[] args) {

        String query = "PID=e*";

        if (args.length == 1) {
            query = args[0];
        }
        
        // get the ItemHandlerClientInterface
        URL serviceAddress = null;
        try {
            serviceAddress = new URL(Constants.DEFAULT_SERVICE_URL);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        ItemHandlerClientInterface c = new ItemHandlerClient(serviceAddress);

        SearchRetrieveRequestType request = new SearchRetrieveRequestType();
        request.setQuery(query);

        // further request parameters could be set, e.g.:
        // request.setStartRecord(new PositiveInteger("21"));
        // request.setMaximumRecords(new NonNegativeInteger("40"));
        request.setRecordPacking(RecordPacking.XML.toString());

        SearchRetrieveResponse response = null;
        try {

            response = c.retrieveItems(request);
        }
        catch (EscidocException e1) {
            e1.printStackTrace();
        }
        catch (InternalClientException e1) {
            e1.printStackTrace();
        }
        catch (TransportException e1) {
            e1.printStackTrace();
        }

        System.out.println("======================================");
        System.out.println("Example for Filter Search:");
        System.out.println("query: " + query);
        System.out.println("RecordPacking: " + RecordPacking.XML.toString());
        System.out.println("Results: " + response.getNumberOfResultingRecords());
        System.out.println();
        
        for (SearchResultRecord record : response.getRecords()) {
            SearchResult result = record.getRecordData();

            if (result.getContent() instanceof Item) {
                Item item = (Item) result.getContent();
                System.out.println("Item: ID[" + item.getObjid() + "], href[" + item.getXLinkHref() + "]");
            }
        }
    }
}
