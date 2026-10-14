package ressources;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import entities.Credentials;
import entities.TokenResponse;
import Activator.JwtService;
@Path("authentication")
public class AuthentificationEndPoint {
    @Context
    private UriInfo uriInfo;

    // ===== 1. Login : MODIFIÉ
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials cred) {
        try {
            authenticate(cred.getUsername(), cred.getPassword());
            return Response.ok(buildTokens(cred.getUsername())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    // ===== 2. Refresh
    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response refresh(@FormParam("refreshToken") String refreshToken) {
        try {
            // Valide le refresh token et le consomme (rotation)
            String username = JwtService.useRefreshToken(refreshToken);
            // Renvoie un nouveau couple de jetons
            return Response.ok(buildTokens(username)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // 3. Logout
    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logout(@FormParam("refreshToken") String refreshToken) {
        JwtService.revokeRefreshToken(refreshToken);
        return Response.noContent().build();
    }

    // génère les deux jetons et les place dans un TokenResponse
    private TokenResponse buildTokens(String username) {
        String issuer = uriInfo.getBaseUri().toString();
        String accessToken = JwtService.generateAccessToken(username, issuer);
        String refreshToken = JwtService.generateRefreshToken(username, issuer);
        return new TokenResponse(accessToken, refreshToken, JwtService.ACCESS_MINUTES
                * 60);
    }

    private void authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            throw new SecurityException("Identifiants invalides");
        }
    }
}