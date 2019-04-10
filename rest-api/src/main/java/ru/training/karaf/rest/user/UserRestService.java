package ru.training.karaf.rest.user;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ru.training.karaf.rest.user.dto.UserDTO;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserRestService {
    
    @GET
    List<UserDTO> getAll();
    
    @POST
    void create(UserDTO user);

    @GET
    @Path("{login}")
    UserDTO get(@PathParam("login") String login);
    
    @DELETE
    @Path("{login}")
    void delete(@PathParam("login") String login);
}
