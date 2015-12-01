/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.rai.myapplication.backend;


import com.example.rai.myapplication.backend.model.SalesData;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.rai.myapplication.backend.OfyService.ofy;

//import com.google.appengine.api.datastore.Cursor;

public class    MyServlet extends HttpServlet {

//    @Override
//    public final void doGet(final HttpServletRequest req,
//                            final HttpServletResponse resp) throws IOException {
//        resp.setContentType("text/plain");
////        removeAllDocumentsFromIndex();
////        if (buildSearchIndexForPlaces()=="null") {
//        resp.getWriter().println(buildSearchIndexForPlaces());
//        return;
////        }
////        resp.getWriter().println("MaintenanceTasks completed");
//    }

    /**
     * Creates the indexes to search for places.
     * @return a boolean indicating the success or failure of the method.
     */
//    @SuppressWarnings({"cast", "unchecked"})
//    private String buildSearchIndexForPlaces() {
//
//
//        removeAllDocumentsFromIndex();
//
//        Cursor cursor = null;
//        int cnt = 0;
//        while (true) {
//            Index index = SQLDatabaseQueryHelper.getIndex();
////        removeAllDocumentsFromIndex();
////            Iterable<Key<SalesData>> keys = ofy().load(SalesData.class).filter("startDate !=", null).keys();
//            Query<SalesData> query = ofy().load().type(SalesData.class).chunk(100)
////                    .filter("Duration!=",null)
//                    ; // should i call 'chunk' or not?
//            if (cursor != null) { // for first time cursor is null,for second (and more) time, cursor is not null
//                query = query.startAt(cursor);
//            }
//            query = query.limit(100);
//
//            final QueryResultIterator<SalesData> it = query.iterator();
//            if (!it.hasNext()) {
//                break;
//            }
////
//            while (it.hasNext()) {
//
//                final SalesData place = it.next();
//                if (true) {
////                    String[] tokens = line.split("\",\"");
////                    tokens.
//                    Document placeAsDocument = SQLDatabaseQueryHelper.buildDocument(
//                            place.getId(), place.getPrice(),
//                            new GeoPoint(place.getLatitude(), place.getLongitude())
//                    );
//
//
//                    //
////            Document placeAsDocument = SQLDatabaseQueryHelper.buildDocument(123L,"abbab",321D,new GeoPoint(50.0D,-1.0D));
//                    try {
//                        index.put(placeAsDocument);
//                    } catch (DatastoreTimeoutException e) {
//                        return "timeout";
//                    } catch (PutException e) {
//                        if (StatusCode.TRANSIENT_ERROR
//                                .equals(e.getOperationResult().getCode())) {
////                        continue;
//                            return "transiend error";
//                        }
//                    }
////                final SalesData entity = it.next();
//                    cnt++; // ok, here we are do nothig! just incrementing 'cnt'
//
//                }
//            }
//            // take current cursor before next iteration
//
//            cursor = it.getCursor();
//            ofy().clear();
//        }
//        return "done";
//    }

    /**
     * Cleans the index of places from all entries.
     */
//    private void removeAllDocumentsFromIndex() {
//        Index index = SQLDatabaseQueryHelper.getIndex();
//        // As the request will only return up to 1000 documents,
//        // we need to loop until there are no more documents in the index.
//        // We batch delete 1000 documents per iteration.
//        final int numberOfDocuments = 10000;
//        while (true) {
//            GetRequest request = GetRequest.newBuilder()
//                    .setReturningIdsOnly(true)
//                    .build();
//
//            ArrayList<String> documentIds = new ArrayList<>(numberOfDocuments);
//            GetResponse<Document> response = index.getRange(request);
//            for (Document document : response.getResults()) {
//                documentIds.add(document.getId());
//            }
//
//            if (documentIds.size() == 0) {
//                break;
//            }
//
//            index.delete(documentIds);
//        }
//    }

//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//        String name = req.getParameter("act");
//        resp.setContentType("text/plain");
////        removeAllDocumentsFromIndex();
////        if (buildSearchIndexForPlaces()=="null") {
//        resp.getWriter().println(buildSearchIndexForPlaces());
//        return;
//    }

}
