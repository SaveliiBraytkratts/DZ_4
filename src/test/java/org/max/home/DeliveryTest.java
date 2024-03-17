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
public class DeliveryTest extends AbstractTest{
    @Test
    @Order(1)
    void getDelivery_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM delivery";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM delivery").addEntity(CustomersEntity.class);
        //then
        Assertions.assertEquals(15, countTableSize);
    }
//    @Test
//    @Order(2)
//    void addDelivery_whenValid_shouldSave() {
//        //given
//        DeliveryEntity entity = new DeliveryEntity();
//        entity.setDeliveryId((short) 16);
//        entity.setOrders((short) 1);
//        entity.setCourierInfo((short) 16);
//        entity.setDateArrived("2023-02-26");
//        entity.setTaken("18:04:36");
//        entity.setPaymentMethod("Cash");
//        //when
//        Session session = getSession();
//        session.beginTransaction();
//        session.persist(entity);
//        session.getTransaction().commit();
//
//        final Query query = getSession()
//                .createSQLQuery("SELECT * FROM delivery WHERE delivery_id="+16).addEntity(DeliveryEntity.class);
//        DeliveryEntity creditEntity = (DeliveryEntity) query.uniqueResult();
//        //then
//        Assertions.assertNotNull(creditEntity);
//        Assertions.assertEquals("Cash", creditEntity.getPaymentMethod());
//    }
    @Test
    @Order(3)
    void deleteDelivery_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM delivery WHERE delivery_id=" + 16).addEntity(DeliveryEntity.class);
        Optional<DeliveryEntity> deliveryEntity = (Optional<DeliveryEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(deliveryEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(deliveryEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM delivery WHERE delivery_id=" + 16).addEntity(DeliveryEntity.class);
        Optional<DeliveryEntity> deliveryEntityAfterDelete = (Optional<DeliveryEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(deliveryEntityAfterDelete.isPresent());
    }
    @Test
    @Order(4)
    void addDelivery_whenNotValid_shouldThrow() {
        //given
        ProductsEntity entity = new ProductsEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
        ;
    }
}
