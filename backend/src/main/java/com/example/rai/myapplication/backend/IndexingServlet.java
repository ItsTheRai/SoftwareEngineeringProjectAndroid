//package com.example.rai.myapplication.backend;
//
//import com.example.rai.myapplication.backend.model.SalesData;
////import com.google.appengine.api.datastore.Cursor;
//import com.google.appengine.api.datastore.Cursor;
//import com.google.appengine.api.datastore.GeoPt;
//import com.google.appengine.api.datastore.QueryResultIterator;
//import com.google.appengine.api.search.Document;
//import com.google.appengine.api.search.GeoPoint;
//import com.google.appengine.api.search.GetRequest;
//import com.google.appengine.api.search.GetResponse;
//import com.google.appengine.api.search.Index;
//import com.google.appengine.api.search.PutException;
//import com.google.appengine.api.search.StatusCode;
//import com.google.appengine.api.datastore.DatastoreTimeoutException;
//import com.googlecode.objectify.cmd.Query;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import static com.example.rai.myapplication.backend.OfyService.ofy;
//public class IndexingServlet extends HttpServlet {
//
//    @Override
//    public final void doGet(final HttpServletRequest req,
//                            final HttpServletResponse resp) throws IOException {
//        resp.setContentType("text/plain");
//        removeAllDocumentsFromIndex();
////        if (buildSearchIndexForPlaces()=="null") {
//            resp.getWriter().println(buildSearchIndexForPlaces());
//            return;
////        }
////        resp.getWriter().println("MaintenanceTasks completed");
//    }
//
//    /**
//     * Creates the indexes to search for places.
//     * @return a boolean indicating the success or failure of the method.
//     */
//    @SuppressWarnings({"cast", "unchecked"})
//    private String buildSearchIndexForPlaces() {
//
//
////        removeAllDocumentsFromIndex();
//
//        Cursor cursor = null;
//        int cnt = 0;
//        while (true) {
//            Index index = SQLDatabaseQueryHelper.getIndex();
//            Query<SalesData> query = OfyService.ofy().load().type(SalesData.class).chunk(1000); // should i call 'chunk' or not?
//            if (cursor != null) { // for first time cursor is null
//                // for second (and more) time, cursor is not null
//                query = query.startAt(cursor);
//            }
//            query = query.limit(1000);
//
//            final QueryResultIterator<SalesData> it = query.iterator();
//            if (!it.hasNext()) {
//                break;
//            }
//
//            while (it.hasNext()) {
//                SalesData place = it.next();
//                Document placeAsDocument = SQLDatabaseQueryHelper.buildDocument(
//                        place.getId(), place.getPostcode(), place.getPrice(),
//                        new GeoPoint(place.getLatitude(),place.getLongitude())
//                );
//
//                try {
//                    index.put(placeAsDocument);
//                } catch (DatastoreTimeoutException e){
//                    return "timeout";
//                }
//                catch (PutException e) {
//                    if (StatusCode.TRANSIENT_ERROR
//                            .equals(e.getOperationResult().getCode())) {
////                        continue;
//                        return "transiend error";
//                    }
//                }
//                catch (Exception e){
//                    return String.valueOf(cnt);
//                }
////                final SalesData entity = it.next();
//                cnt++; // ok, here we are do nothig! just incrementing 'cnt'
//            }
//
//            // take current cursor before next iteration
//            cursor = it.getCursor();
//            ofy().clear();
//        }
//        return "done";
//    }
//
//    /**
//     * Cleans the index of places from all entries.
//     */
//    private void removeAllDocumentsFromIndex() {
//        Index index = SQLDatabaseQueryHelper.getIndex();
//        // As the request will only return up to 1000 documents,
//        // we need to loop until there are no more documents in the index.
//        // We batch delete 1000 documents per iteration.
//        final int numberOfDocuments = 2000;
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
//}