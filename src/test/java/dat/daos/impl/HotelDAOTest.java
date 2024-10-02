package dat.daos.impl;

import dat.persistence.HibernateConfig;
import dat.rest.dtos.HotelDTO;
import dat.persistence.daos.impl.HotelDAO;
import dat.persistence.entities.Hotel;
import dat.persistence.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
class HotelDAOTest {
    private static EntityManagerFactory emf;
    private static HotelDAO dao;
    Hotel h1, h2, h3;
    Room r1, r2, r3;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = HotelDAO.getInstance(emf);
    }

    @BeforeEach
    void init() {
        h1 = new Hotel("Hotel 1", "Address 1", Hotel.HotelType.BUDGET);
        h2 = new Hotel("Hotel 2", "Address 2", Hotel.HotelType.LUXURY);
        h3 = new Hotel("Hotel 3", "Address 3", Hotel.HotelType.BUDGET);
        r1 = new Room(1, new BigDecimal(1000), Room.RoomType.SINGLE);
        r2 = new Room(2, new BigDecimal(1500), Room.RoomType.SINGLE);
        r3 = new Room(3, new BigDecimal(2000), Room.RoomType.DOUBLE);
        try(EntityManager em = emf.createEntityManager()) {
            h1.addRoom(r1);
            h1.addRoom(r2);
            h2.addRoom(r3);
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    @DisplayName("Read hotel by id")
    void read() {
       HotelDTO actual = dao.read(h1.getId());
         HotelDTO expected = new HotelDTO(h1);
            assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Read all hotels")
    void readAll() {
        assertEquals(3, dao.readAll().size());
    }

    @Test
    @DisplayName("Create hotel")
    void create() {
        HotelDTO hotelDTO = new HotelDTO("Hotel 4", "Address 4", Hotel.HotelType.BUDGET);
        HotelDTO actual = dao.create(hotelDTO);
        HotelDTO expected = new HotelDTO("Hotel 4", "Address 4", Hotel.HotelType.BUDGET);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update hotel")
    void update() {
        HotelDTO hotelDTO = new HotelDTO("Hotel 4", "Address 4", Hotel.HotelType.BUDGET);
        HotelDTO actual = dao.update(h1.getId(), hotelDTO);
        HotelDTO expected = new HotelDTO("Hotel 4", "Address 4", Hotel.HotelType.BUDGET);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete hotel")
    void delete() {
        dao.delete(h1.getId());
        try{
            dao.read(h1.getId());
            fail("Hotel not deleted");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Validate primary key")
    void validatePrimaryKey() {
        assertTrue(dao.validatePrimaryKey(h1.getId()));
    }
}