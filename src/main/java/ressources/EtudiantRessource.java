package ressources;

import entities.Etudiant;
import entities.Option;
import metiers.EtudiantBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/etudiants")
public class EtudiantRessource {

    EtudiantBusiness etudiantBusiness = new EtudiantBusiness();

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtudiants() {
        List<Etudiant> etudiants = etudiantBusiness.getAllEtudiants();
        return Response.ok(etudiants).build();
    }

    @GET
    @Path("/{identifiant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiantByIdentifiant(@PathParam("identifiant") String identifiant) {
        Etudiant etudiant = etudiantBusiness.getEtudiantByIdentifiant(identifiant);
        if (etudiant == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Etudiant with identifiant " + identifiant + " not found")
                    .build();
        }
        return Response.ok(etudiant).build();
    }

    @GET
    @Path("/option/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiantsByOption(@PathParam("code") int code) {
        Option option = new Option();
        option.setCodeOption(code);
        List<Etudiant> etudiants = etudiantBusiness.getEtudiantsByOption(option);
        return Response.ok(etudiants).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant etudiant) {
        boolean added = etudiantBusiness.addEtudiant(etudiant);
        if (added) {
            return Response.status(Response.Status.CREATED)
                    .entity(etudiant)
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to add etudiant")
                .build();
    }

    @PUT
    @Path("/update/{identifiant}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("identifiant") String identifiant, Etudiant etudiant) {
        boolean updated = etudiantBusiness.updateEtudiant(identifiant, etudiant);
        if (updated) {
            return Response.ok(etudiant).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Etudiant with identifiant " + identifiant + " not found")
                .build();
    }

    @DELETE
    @Path("/delete/{identifiant}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEtudiant(@PathParam("identifiant") String identifiant) {
        boolean deleted = etudiantBusiness.deleteEtudiant(identifiant);
        if (deleted) {
            return Response.ok("Etudiant with identifiant " + identifiant + " deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Etudiant with identifiant " + identifiant + " not found")
                .build();
    }
}
