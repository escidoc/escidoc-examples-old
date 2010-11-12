package org.escidoc.workingWithClientLib.ClassMapping.sb;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.search.SearchResultRecord;
import de.escidoc.core.resources.sb.search.SearchRetrieveResponse;
import de.escidoc.core.resources.sb.search.records.SearchResultRecordRecord;

/**
 * Example how to search. Some example queries are provided, parameters can be
 * hardcoded into existing variables.
 * 
 * @author MRO
 * 
 */
public class SearchExamples {

	/**
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		try {
			String query;
			String contextObjId;
			String contentmodelObjId;

			// Search for a specific element
			query = "escidoc.objid=escidoc:4003";
			search(query);

			// Search for items created after a given timestamp
			String timestamp = "2010-10-28T09:27:09.629Z";
			query = "escidoc.objecttype=item AND escidoc.creation-date >= "
					+ timestamp;
			search(query);

			// Search for items referencing a given context and content-model
			contextObjId = "escidoc:4001";
			contentmodelObjId = "escidoc:5001";
			query = "escidoc.objecttype=item AND escidoc.context.objid = "
					+ contextObjId + " AND escidoc.content-model.objid = "
					+ contentmodelObjId;
			search(query);

			// Search for containers referencing a given context and
			// content-model
			contextObjId = "escidoc:4001";
			contentmodelObjId = "escidoc:5001";
			query = "escidoc.objecttype=container AND escidoc.context.objid = "
					+ contextObjId + " AND escidoc.content-model.objid = "
					+ contentmodelObjId;
			search(query);

		} catch (EscidocClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Search with the provided query. Results' Objids are printed to console.
	 * 
	 * @param query
	 *            The query to search with
	 * @throws EscidocClientException
	 */
	private static void search(String query) throws EscidocClientException {

		// Get a SearchHandlerClient
		SearchHandlerClient shc = new SearchHandlerClient();
		shc.setTransport(TransportProtocol.SOAP);

		// Query the query
		System.out.println("Query: " + query);
		SearchRetrieveResponse response = shc.search(query, null);
		System.out.println(response.getNumberOfResultingRecords()
				+ " result(s) found.");

		// Iterate over response records
		for (Record<?> record : response.getRecords()) {
			if (record instanceof SearchResultRecordRecord) {
				// Cast RecordData accordingly
				SearchResultRecord data = ((SearchResultRecordRecord) record)
						.getRecordData();
				// Get and print Objid
				System.out.println(data.getContent().getObjid());
			}
		}
		System.out.println();
	}
}
