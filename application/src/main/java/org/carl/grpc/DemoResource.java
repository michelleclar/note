package org.carl.grpc;


import demo.Demo;
import demo.DemoService;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/grpc")
public class DemoResource {
    @GrpcClient("demo")
    DemoService demo;

    @GET
    @Path("demo")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {

        return demo.sendMessage(Demo.Request.newBuilder().setMessage("hay").build()).onItem().transform(heReply -> heReply.getReply());
    }
}
