package ressources;

import entities.Option;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/options")
public class OptionRessource {

    OptionBusiness optionBusiness = new OptionBusiness();

    // GET /options/list
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptions() {
        List<Option> options = optionBusiness.getListeOptions();
        return Response.ok(options).build();
    }

    // GET /options/{code}
    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptionByCode(@PathParam("code") int code) {
        Option option = optionBusiness.getOptionByCode(code);
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Option with code " + code + " not found")
                    .build();
        }
        return Response.ok(option).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOption(Option option) {
        boolean added = optionBusiness.addOption(option);
        if (added) {
            return Response.status(Response.Status.CREATED)
                    .entity(option)
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to add option")
                .build();
    }

    @PUT
    @Path("/update/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOption(@PathParam("code") int code, Option option) {
        boolean updated = optionBusiness.updateOption(code, option);
        if (updated) {
            return Response.ok(option).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Option with code " + code + " not found")
                .build();
    }
    @DELETE
    @Path("/delete/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOption(@PathParam("code") int code) {
        boolean deleted = optionBusiness.deleteOption(code);
        if (deleted) {
            return Response.ok("Option with code " + code + " deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Option with code " + code + " not found")
                .build();
    }
}
