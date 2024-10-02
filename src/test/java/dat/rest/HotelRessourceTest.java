package dat.rest;

import dat.persistence.HibernateConfig;
import dat.persistence.daos.impl.HotelDAO;
import dat.persistence.entities.Hotel;
import dat.persistence.entities.Room;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HotelRessourceTest {
    private static EntityManagerFactory emf;
    private static HotelDAO hotelDAO;
    Hotel h1, h2, h3;
    Room r1, r2, r3;
    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        hotelDAO = HotelDAO.getInstance(emf);
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
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void afterAll() {
        HibernateConfig.setTest(false);
        emf.close();
        emf = null;
    }
}