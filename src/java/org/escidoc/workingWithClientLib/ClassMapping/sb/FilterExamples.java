package org.escidoc.workingWithClientLib.ClassMapping.sb;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;
import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.ItemHandlerClient;

import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ContainerHandlerClientInterface;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.Record.RecordPacking;

import de.escidoc.core.resources.sb.search.SearchRetrieveResponse;
import de.escidoc.core.resources.sb.search.records.ContainerRecord;
import de.escidoc.core.resources.sb.search.records.ItemRecord;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

/**
 * Example how to filter. Some example queries are provided, parameters can be
 * hardcoded into existing variables.
 * 
 * @author MRO
 * 
 */
public class FilterExamples {

	/**
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		try {
			String query;
			String timestamp;
			String contextObjId;
			String contentmodelObjId;
			String name;
			String title;

			// Login
			Authentication auth = new Authentication(
					Constants.DEFAULT_SERVICE_URL, Constants.USER_NAME,
					Constants.USER_PASSWORD);

			// Filter items created after a given timestamp
			// Timestamp must be quoted
			timestamp = "\"2010-10-28T09:27:09.629Z\"";
			query = "\"/properties/creation-date\" >= " + timestamp;
			filterItems(query, auth);

			// Filter items referencing a given context and content-model
			contextObjId = "escidoc:4001";
			contentmodelObjId = "escidoc:5001";
			query = "\"/properties/context/id\" = " + contextObjId
					+ " AND \"/properties/content-model/id\" = "
					+ contentmodelObjId;
			filterItems(query, auth);

			// Filter containers with status released.
			// Sort ascending by creation-date
			query = "\"/properties/public-status\" = released sortby \"/properties/creation-date\" /sort.ascending";
			filterContainer(query, auth);

			// Filter containers having a given name
			name = "Exmaple Container"; // sic
			query = "\"/properties/name\" = \"" + name + "\"";
			filterContainer(query, auth);

			// Filter containers having a given metadata title
			title = "Exmaple Container";
			query = "\"/md-records/md-record/myMdRecord/title\"=\"" + title
					+ "\"";
			filterContainer(query, auth);

			// Filter containers referencing a given content-model
			contentmodelObjId = "escidoc:5001";
			query = "\"/properties/content-model/id\" = " + contentmodelObjId;
			filterContainer(query, auth);

			// Filter containers created after a given timestamp
			// Timestamp must be quoted
			timestamp = "\"2010-10-28T09:27:09.629Z\"";
			query = "\"/properties/creation-date\" >= " + timestamp;
			filterContainer(query, auth);

			// Filter containers created after a given timestamp
			// Act as non-authenticated user
			// Timestamp must be quoted
			timestamp = "\"2010-10-28T09:27:09.629Z\"";
			query = "\"/properties/creation-date\" >= " + timestamp;
			filterContainer(query, null);

			// Filter containers with status released.
			// Show only one container per page, start at 2nd page
			query = "\"/properties/public-status\"=released";
			filterContainer(query, auth, new NonNegativeInteger("1"),
					new PositiveInteger("2"));

		} catch (EscidocClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Filter Items with the provided query. Results' Objids are printed to
	 * console.
	 * 
	 * @param query
	 *            The query to filter with
	 * 
	 * @throws EscidocClientException
	 */
	private static void filterItems(String query, Authentication auth)
			throws EscidocClientException {

		// Get an ItemHandlerClient to perform filtering
		ItemHandlerClientInterface ihc = new ItemHandlerClient(
				auth.getServiceAddress());
		ihc.setHandle(auth.getHandle());
		ihc.setTransport(TransportProtocol.SOAP);

		// Construct the request
		SearchRetrieveRequestType request = new SearchRetrieveRequestType();
		request.setQuery(query);
		request.setRecordPacking(RecordPacking.xml.name());

		// Query the request
		System.out.println("Query: " + query);
		SearchRetrieveResponse response = ihc.retrieveItems(request);
		System.out.println(response.getNumberOfResultingRecords()
				+ " result(s) found.");

		// Iterate over response records
		for (Record<?> record : response.getRecords()) {
			if (record instanceof ItemRecord) {
				// Cast RecordData accordingly
				Item data = ((ItemRecord) record).getRecordData();
				// Get and print Objid
				System.out.println(data.getObjid());
			}
		}
		System.out.println();
	}

	/**
	 * Auxiliary method. Adds missing parameter and calls the method doing the
	 * actual work.
	 * 
	 * @param query
	 *            The query to filter with
	 * @throws EscidocException
	 * @throws InternalClientException
	 * @throws TransportException
	 */
	private static void filterContainer(String query, Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {
		filterContainer(query, auth, null, null);
	}

	/**
	 * Filter Containers with the provided query. Results' Objids are printed to
	 * console. Deals with paging, when parameters are supplied.
	 * 
	 * @param query
	 *            The query to filter with
	 * @param maxRecords
	 *            The maximum number of records to be returned
	 * @param startRecord
	 *            The number of the first record to be returned
	 * @throws EscidocException
	 * @throws InternalClientException
	 * @throws TransportException
	 */
	private static void filterContainer(String query, Authentication auth,
			NonNegativeInteger maxRecords, PositiveInteger startRecord)
			throws EscidocException, InternalClientException,
			TransportException {

		// Get an ContainerHandlerClient to perform filtering
		ContainerHandlerClientInterface chc = new ContainerHandlerClient();
		// Authenticate if possible
		if (auth != null) {
			chc = new ContainerHandlerClient(auth.getServiceAddress());
			chc.setHandle(auth.getHandle());
		}
		chc.setTransport(TransportProtocol.REST);

		// Construct the request
		SearchRetrieveRequestType request = new SearchRetrieveRequestType();
		request.setQuery(query);
		if (maxRecords != null) {
			System.out.println("maxRecords=" + maxRecords);
			request.setMaximumRecords(maxRecords);
		}
		if (startRecord != null) {
			System.out.println("startRecord=" + startRecord);
			request.setStartRecord(startRecord);
		}
		request.setRecordPacking(RecordPacking.xml.name());

		// Query the request
		System.out.println("Query: " + query);
		SearchRetrieveResponse response = chc.retrieveContainers(request);
		System.out.println(response.getNumberOfResultingRecords()
				+ " result(s) found.");

		// Iterate over response records
		for (Record<?> record : response.getRecords()) {
			if (record instanceof ContainerRecord) {
				// Cast RecordData accordingly
				Container data = ((ContainerRecord) record).getRecordData();
				// Get and print Objid
				System.out.println(data.getObjid());
			}
		}
		System.out.println();
	}
}
