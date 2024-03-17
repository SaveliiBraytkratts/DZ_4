package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.max.seminar.ClientEntity;


import javax.persistence.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest extends AbstractTest{
    @Test
    @Order(1)
    void getOrders_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM orders";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM orders").addEntity(ClientEntity.class);
        //then
        Assertions.assertEquals(15, countTableSize);

    }
//    @Test
//    @Order(2)
//    void addOrders_whenValid_shouldSave() {
//        //given
//        OrdersEntity entity = new OrdersEntity();
//        entity.setOrderId((short) 16);
//        entity.setDateGet("now");
//        //when
//        Session session = getSession();
//        session.beginTransaction();
//        session.persist(entity);
//        session.getTransaction().commit();
//
//        final Query query = getSession()
//                .createSQLQuery("SELECT * FROM orders WHERE order_id=" + 16).addEntity(OrdersEntity.class);
//        OrdersEntity creditEntity = (OrdersEntity) query.uniqueResult();
//        //then
//        Assertions.assertNotNull(creditEntity);
//        Assertions.assertEquals("now", creditEntity.getDateGet());
//    }
    @Test
    @Order(3)
    void deleteOrders_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM orders WHERE order_id=" + 16).addEntity(OrdersEntity.class);
        Optional<OrdersEntity> ordersEntity = (Optional<OrdersEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(ordersEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(ordersEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM orders WHERE order_id=" + 16).addEntity(OrdersEntity.class);
        Optional<OrdersEntity> ordersEntityAfterDelete = (Optional<OrdersEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(ordersEntityAfterDelete.isPresent());
    }
    @Test
    @Order(4)
    void addOrders_whenNotValid_shouldThrow() {
        //given
        OrdersEntity entity = new OrdersEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
        ;
    }
}