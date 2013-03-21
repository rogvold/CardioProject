package ru.cardio.web.webservices;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import ru.cardio.core.managers.CardioSessionManagerLocal;
import ru.cardio.exceptions.CardioException;
import ru.cardio.json.entity.SimpleRatesData;
import ru.cardio.web.json.utils.CardioExceptionWrapper;
import ru.cardio.web.json.utils.SimpleResponseWrapper;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Stateless
@Path("rates")
public class RatesUploading {

    private static final String YES = "1";
    private static final String NO = "2";
    
    @Context
    private UriInfo context;
    @EJB
    CardioSessionManagerLocal cardMan;

    /**
     * Creates a new instance of RatesUploading
     */
    public RatesUploading() {
    }

    /**
     * Retrieves representation of an instance of
     * ru.cardio.web.webservices.RatesUploading
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("upload")
    @Produces("application/json")
    public String uploadRates(@QueryParam("json") String json) throws CardioException {
        try {
            SimpleRatesData sdr = SimpleRatesData.fromJson(json);
            cardMan.addRates(sdr);
        } catch (CardioException e) {
            return CardioExceptionWrapper.wrapException(e);
        }
        return SimpleResponseWrapper.getJsonResponse(YES);
    }

    @POST
    @Path("sync")
    @Produces("application/json")
    public String syncRates(@QueryParam("json") String json) throws CardioException {
        try {
            SimpleRatesData sdr = SimpleRatesData.fromJson(json);
            sdr.setCreate(0);
            cardMan.syncRates(sdr);
            System.out.println("rates synchronized");
        } catch (CardioException e) {
            return CardioExceptionWrapper.wrapException(e);
        }
        return SimpleResponseWrapper.getJsonResponse(YES);
    }

    /**
     * PUT method for updating or creating an instance of RatesUploading
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
