package dat.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat.persistence.HibernateConfig;
import dat.persistence.daos.impl.HotelDAO;
import dat.persistence.entities.Hotel;
import dat.persistence.entities.Room;
import dat.rest.dtos.HotelDTO;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class HotelRessourceTest {
    private static EntityManagerFactory emf;
    private static HotelDAO hotelDAO;
    Hotel h1, h2, h3;
    Room r1, r2, r3;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpAll() {
        RestAssured.baseURI = "http://localhost:7777/api/";
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        hotelDAO = HotelDAO.getInstance(emf);
        ApplicationConfig.startServer(7777);
    }

    @BeforeEach
    void setUp() {
        h1 = new Hotel("Hotel1", "Copenhagen", Hotel.HotelType.BUDGET);
        h2 = new Hotel("Hotel2", "Copenhagen", Hotel.HotelType.LUXURY);
        h3 = new Hotel("Hotel3", "Copenhagen", Hotel.HotelType.STANDARD);
        r1 = new Room(101, new BigDecimal(1000), Room.RoomType.SINGLE);
        r2 = new Room(102, new BigDecimal(1500), Room.RoomType.DOUBLE);
        r3 = new Room(103, new BigDecimal(1500), Room.RoomType.SUITE);
        h1.addRoom(r1);
        h2.addRoom(r2);
        h2.addRoom(r3);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void afterAll() {
        HibernateConfig.setTest(false);
        emf.close();
//        emf = null;
    }

    @Test
    @DisplayName("Get all hotels")
    public void testGettingAllHotels() {
        System.out.println("Testing is server UP");
        given().when().get("/hotel").then().statusCode(200);
    }

    @Test
    @DisplayName("Get a hotel by id")
    public void testGettingAHotel() {
        System.out.println("Testing is server UP");
        given().when().get("/hotel/" + h1.getId())
                .then()
                .statusCode(200)
                .body("hotelName", equalTo("Hotel1"));
    }

    @Test
    @DisplayName("Create a hotel")
    public void testCreatingAHotel() {
        System.out.println("Testing is server UP");
        HotelDTO hotelDTO = new HotelDTO("Hotel4", "Copenhagen", Hotel.HotelType.BUDGET);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(hotelDTO);
        } catch (org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }
        given()
                .accept("application/json")
                .header("Content-Type", "application/json")
                .body(json)
                .when().post("/hotel")
                .then().log().all()
                .body("hotelName", equalTo("Hotel4"))
                .body("hotelType", equalTo("BUDGET"))
                .statusCode(201);
    }

    @Test
    @DisplayName("Update a hotel")
    public void testUpdatingAHotel() {
        HotelDTO hotelDTO = new HotelDTO("Hotel4", "Copenhagen", Hotel.HotelType.BUDGET);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(hotelDTO);
        } catch (org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }
        given()
                .accept("application/json")
                .header("Content-Type", "application/json")
                .body(json)
                .when().put("/hotel/" + h1.getId())
                .then().log().all()
                .body("hotelName", equalTo("Hotel4"))
                .body("hotelType", equalTo("BUDGET"))
                .statusCode(200);
    }

    @Test
    @DisplayName("Delete a hotel")
    public void testDeletingAHotel() {
        given().when().delete("/hotel/" + h1.getId())
                .then().statusCode(204);
    }
}