package dat.rest.routes;

import dat.rest.controllers.impl.RoomController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRoute {

    private final RoomController roomController = new RoomController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/hotel/{id}", roomController::create);
            get("/hotel/{id}", roomController::getByHotel);
            get("/", roomController::readAll);
            get("/{id}", roomController::read);
            put("/{id}", roomController::update);
            delete("/{id}", roomController::delete);
        };
    }
}
