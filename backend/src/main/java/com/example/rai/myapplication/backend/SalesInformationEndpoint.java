package com.example.rai.myapplication.backend;

import com.example.rai.myapplication.backend.model.SalesData;
import com.example.rai.myapplication.backend.model.SalesDataShort;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.GeoPt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import static com.googlecode.objectify.ObjectifyService.ofy;


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

    /**
     * Returns the {@link SalesData} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code SalesData} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "salesInformation/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public SalesData get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting SalesData with ID: " + id);
        SalesData salesInformation = ofy().load().type(SalesData.class).id(id).now();
        if (salesInformation == null) {
            throw new NotFoundException("Could not find SalesData with ID: " + id);
        }
        return salesInformation;
    }

    @ApiMethod(
            name ="getPointsInRange",
            httpMethod = ApiMethod.HttpMethod.GET)
    public List<SalesDataShort> getPointsInRange(@Named("rangeInMiles") float rangeInMiles, @Named("latitude")float latitude,
                                                        @Named("longitude")float longitude, @Named("maxLength") int maxLength){
        //returns point in range of the user using helper class
        List<SalesDataShort> places = NearPlacesFinder.getPlaces(new GeoPt(latitude, longitude), rangeInMiles, maxLength);
        return places;
    }

    /**
     * Inserts a new {@code SalesData}.
     */
    @ApiMethod(
            name = "insert",
            path = "salesData",
            httpMethod = ApiMethod.HttpMethod.POST)
    public SalesData insert(SalesData salesData) throws NotFoundException {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that salesData.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        checkExists(salesData.getId());
        ofy().save().entity(salesData).now();
        logger.info("Created SalesData with ID: " + salesData.getId());
        return ofy().load().entity(salesData).now();
    }

    @ApiMethod(
            name = "list",
            path = "salesInformation",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<SalesData> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<SalesData> query = ofy().load().type(SalesData.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<SalesData> queryIterator = query.iterator();
        List<SalesData> salesDataList = new ArrayList<SalesData>(limit);
        while (queryIterator.hasNext()) {
            salesDataList.add(queryIterator.next());
        }
        return CollectionResponse.<SalesData>builder().setItems(salesDataList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(SalesData.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find SalesData with ID: " + id);
        }
    }
}