package com.example.rai.myapplication.backend;/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.example.rai.myapplication.backend.model.SalesData;
import com.example.rai.myapplication.backend.model.SalesDataShort;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServlet for handling maintenance tasks.
 */
public class IndexingServlet extends HttpServlet {

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        if (!buildSearchIndexForPlaces()) {
            resp.getWriter().println(
                    "MaintenanceTasks failed. Try again by refreshing.");
            return;
        }
        resp.getWriter().println("MaintenanceTasks completed");
    }

    /**
     * Creates the indexes to search for places.
     * @return a boolean indicating the success or failure of the method.
     */
    @SuppressWarnings({"cast", "unchecked"})
    private boolean buildSearchIndexForPlaces() {
        Index index = NearPlacesFinder.getIndex();
//
        removeAllDocumentsFromIndex();
//
        List<SalesData> places = OfyService.ofy().load().type(SalesData.class).list();

        try {
            for (SalesData place : places) {
                Document placeAsDocument = NearPlacesFinder.buildDocument(
                        place.getId(), place.getPostcode(), place.getPrice(),
                        new GeoPt(place.getLatitude(),place.getLongitude()));

                try {
                    index.put(placeAsDocument);
                } catch (PutException e) {
                    if (StatusCode.TRANSIENT_ERROR
                            .equals(e.getOperationResult().getCode())) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Cleans the index of places from all entries.
     */
    private void removeAllDocumentsFromIndex() {
        Index index = NearPlacesFinder.getIndex();
        // As the request will only return up to 1000 documents,
        // we need to loop until there are no more documents in the index.
        // We batch delete 1000 documents per iteration.
        final int numberOfDocuments = 1000;
        while (true) {
            GetRequest request = GetRequest.newBuilder()
                    .setReturningIdsOnly(true)
                    .build();

            ArrayList<String> documentIds = new ArrayList<>(numberOfDocuments);
            GetResponse<Document> response = index.getRange(request);
            for (Document document : response.getResults()) {
                documentIds.add(document.getId());
            }

            if (documentIds.size() == 0) {
                break;
            }

            index.delete(documentIds);
        }
    }
}