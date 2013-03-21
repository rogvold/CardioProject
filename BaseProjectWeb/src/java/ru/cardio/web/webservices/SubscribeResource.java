package ru.cardio.web.webservices;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import ru.cardio.core.managers.SubscriptionManagerLocal;
import ru.cardio.exceptions.SubscriptionException;
import ru.cardio.web.json.utils.CardioExceptionWrapper;
import ru.cardio.web.json.utils.SimpleResponseWrapper;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("subscribe")
@Stateless
public class SubscribeResource {

    @Context
    private UriInfo context;

    @EJB
    SubscriptionManagerLocal sMan;
    
    /**
     * Creates a new instance of SubscribeResource
     */
    public SubscribeResource() {
    }

    /**
     * Retrieves representation of an instance of ru.cardio.web.webservices.SubscribeResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("subscribe")
    @Produces("application/json")
    public String subscribe(@QueryParam("email") String email){
        try {
            sMan.saveSubscription(email);
        } catch (SubscriptionException e) {
            return CardioExceptionWrapper.wrapException(e);
        }
        return SimpleResponseWrapper.getJsonResponse("Thank you!");
    }
    
    /**
     * PUT method for updating or creating an instance of SubscribeResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
