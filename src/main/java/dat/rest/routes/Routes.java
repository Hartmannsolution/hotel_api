package dat.rest.routes;

import dat.utils.Populate;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;


import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final HotelRoute hotelRoute = new HotelRoute();
    private final RoomRoute roomRoute = new RoomRoute();
    private final Populate populate = new Populate();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/hotel", hotelRoute.getRoutes());
                path("/room", roomRoute.getRoutes());
                path("/populate", ()->{
                    get("/", ctx -> populate.createHotels());
                });
        };
    }
}
