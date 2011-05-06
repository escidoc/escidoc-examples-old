package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.ExplainRequestType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.explain.ExplainResponse;
import de.escidoc.core.resources.sb.explain.Index;

/**
 * Example for SRW Search.
 * 
 * @author JHE
 * 
 */
public class SRWExplain {

    /**
     * @param args
     */
    public static void main(String[] args) {

        
        ExplainRequestType request = new ExplainRequestType();
        
        URL serviceAddress = null;
        try {
//            serviceAddress = new URL(Constants.DEFAULT_SERVICE_URL);
          serviceAddress = new URL("http://localhost:8080/srw/search/escidoc_all");  
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
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
        
        Record<de.escidoc.core.resources.sb.explain.Explain> record = response.getRecord();
        de.escidoc.core.resources.sb.explain.Explain data = record.getRecordData();

        List<Index> indexes = data.getIndexInfo().getIndexes();
        
            System.out.println("\n=========================\n");
            System.out.println("Test SRW Explain: ");
            System.out.println("RecordPacking: " + RecordPacking.XML.toString());
            System.out.println("\n=========================\n");
            System.out.println("available search fields for Database 'escidoc_all':\n");
            for (Index index : indexes){
                System.out.println(index.getTitle());
            }
            
            System.out.println("\n");
    }
}
