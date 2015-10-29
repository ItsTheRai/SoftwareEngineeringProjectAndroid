package com.example.rai.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "salesInformationApi",
        version = "v1",
        resource = "salesInformation",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.rai.example.com",
                ownerName = "backend.myapplication.rai.example.com",
                packagePath = ""
        )
)
public class SalesInformationEndpoint {

    private static final Logger logger = Logger.getLogger(SalesInformationEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(SalesInformation.class);
    }

    /**
     * Returns the {@link SalesInformation} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code SalesInformation} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "salesInformation/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public SalesInformation get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting SalesInformation with ID: " + id);
        SalesInformation salesInformation = ofy().load().type(SalesInformation.class).id(id).now();
        if (salesInformation == null) {
            throw new NotFoundException("Could not find SalesInformation with ID: " + id);
        }
        return salesInformation;
    }

    /**
     * Inserts a new {@code SalesInformation}.
     */
    @ApiMethod(
            name = "insert",
            path = "salesInformation",
            httpMethod = ApiMethod.HttpMethod.POST)
    public SalesInformation insert(SalesInformation salesInformation) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that salesInformation.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(salesInformation).now();
        logger.info("Created SalesInformation with ID: " + salesInformation.getId());

        return ofy().load().entity(salesInformation).now();
    }

    /**
     * Updates an existing {@code SalesInformation}.
     *
     * @param id               the ID of the entity to be updated
     * @param salesInformation the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code SalesInformation}
     */
    @ApiMethod(
            name = "update",
            path = "salesInformation/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public SalesInformation update(@Named("id") Long id, SalesInformation salesInformation) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(salesInformation).now();
        logger.info("Updated SalesInformation: " + salesInformation);
        return ofy().load().entity(salesInformation).now();
    }

    /**
     * Deletes the specified {@code SalesInformation}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code SalesInformation}
     */
    @ApiMethod(
            name = "remove",
            path = "salesInformation/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(SalesInformation.class).id(id).now();
        logger.info("Deleted SalesInformation with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "salesInformation",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<SalesInformation> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<SalesInformation> query = ofy().load().type(SalesInformation.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<SalesInformation> queryIterator = query.iterator();
        List<SalesInformation> salesInformationList = new ArrayList<SalesInformation>(limit);
        while (queryIterator.hasNext()) {
            salesInformationList.add(queryIterator.next());
        }
        return CollectionResponse.<SalesInformation>builder().setItems(salesInformationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(SalesInformation.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find SalesInformation with ID: " + id);
        }
    }
}