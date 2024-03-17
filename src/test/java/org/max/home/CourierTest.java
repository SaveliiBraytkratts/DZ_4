package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;


import javax.persistence.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierTest extends AbstractTest{
    @Test
    @Order(1)
    void getCourier_info_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM courier_info";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM courier_info").addEntity(CustomersEntity.class);
        //then
        Assertions.assertEquals(4, countTableSize);
    }
    @Test
    @Order(2)
    void addCourier_info_whenValid_shouldSave() {
        //given
        CourierInfoEntity entity = new CourierInfoEntity();
        entity.setCourierId((short) 5);
        entity.setFirstName("Федор");
        entity.setLastName("Федорович");
        entity.setPhoneNumber("+7 960 800 9000");
        entity.setDeliveryType("foot");
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id="+5).addEntity(CourierInfoEntity.class);
        CourierInfoEntity creditEntity = (CourierInfoEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(creditEntity);
        Assertions.assertEquals("foot", creditEntity.getDeliveryType());
    }
    @Test
    @Order(3)
    void deleteCourier_info_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
        Optional<CourierInfoEntity> productsEntity = (Optional<CourierInfoEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(productsEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(productsEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
        Optional<CourierInfoEntity> productsEntityAfterDelete = (Optional<CourierInfoEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(productsEntityAfterDelete.isPresent());
    }
    @Test
    @Order(4)
    void addCourier_info_whenNotValid_shouldThrow() {
        //given
        CourierInfoEntity entity = new CourierInfoEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
        ;
    }
}