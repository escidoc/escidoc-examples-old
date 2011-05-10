package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;
import org.escidoc.Constants;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.search.SearchResult;
import de.escidoc.core.resources.sb.search.SearchResultRecord;
import de.escidoc.core.resources.sb.search.SearchRetrieveResponse;

/**
 * Example for SRW Search.
 * 
 * @author JHE
 * 
 */
public class SRWEnhancedSearch {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String query = "PID=e*";

        if (args.length == 1) {
            query = args[0];
        }

       // SRW Search
        URL serviceAddress = null;
        try {
            serviceAddress = new URL(Constants.DEFAULT_SERVICE_URL + "/srw/search/escidoc_all");  
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SearchHandlerClient c = new SearchHandlerClient(serviceAddress);

        SearchRetrieveResponse response = null;
        try {
           
           SearchRetrieveRequestType request = new SearchRetrieveRequestType();
           request.setQuery(query);
           request.setStartRecord(new PositiveInteger("1"));
           request.setMaximumRecords(new NonNegativeInteger("5"));
           
           response = c.search(request, null);
           
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

        System.out.println("======================================");
        System.out.println("Example for SRW advanced Search:");
        System.out.println("query = " + query);
        System.out.println("RecordPacking: " + RecordPacking.XML.toString());
        System.out.println("Results: " + response.getNumberOfResultingRecords());
        System.out.println();

        for (final SearchResultRecord record : response.getRecords()) {

            final SearchResult data = record.getRecordData();

            if (data.getContent() instanceof Resource) {

                Resource res = (Resource) data.getContent();

                System.out.println(res.getResourceType().name() + ": ID[" + res.getObjid() + "], href["
                    + res.getXLinkHref() + "], score[" + data.getScore() + "]");
            }
        }
    }
}
