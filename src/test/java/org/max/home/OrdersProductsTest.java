package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.max.seminar.ClientEntity;


import javax.persistence.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrdersProductsTest extends AbstractTest{
    @Test
    @Order(1)
    void getOrders_products_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM orders_products";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM orders_products").addEntity(ClientEntity.class);
        //then
        Assertions.assertEquals(23, countTableSize);

    }
//    @Test
//    @Order(2)
//    void addOrders_products_whenValid_shouldSave() {
//        //given
//        OrdersProductsEntity entity = new OrdersProductsEntity();
//        entity.setOrderId((short) 24);
//        entity.setProductId((short) 24);
//        entity.setQuantity((short) 24);
//
//        //when
//        Session session = getSession();
//        session.beginTransaction();
//        session.persist(entity);
//        session.getTransaction().commit();
//
//        final Query query = getSession()
//                .createSQLQuery("SELECT * FROM orders_products WHERE order_id="+24).addEntity(OrdersProductsEntity.class);
//        OrdersProductsEntity creditEntity = (OrdersProductsEntity) query.uniqueResult();
//        //then
//        Assertions.assertNotNull(creditEntity);
//        Assertions.assertEquals("24", creditEntity.getOrderId());
//    }
//    @Test
//    @Order(3)
//    void deleteOrders_products_whenValid_shouldDelete() {
//        //given
//        final Query query = getSession()
//                .createSQLQuery("SELECT * FROM orders_products WHERE order_id=" + 24).addEntity(OrdersProductsEntity.class);
//        Optional<OrdersProductsEntity> orders_productsEntity = (Optional<OrdersProductsEntity>) query.uniqueResultOptional();
//        Assumptions.assumeTrue(orders_productsEntity.isPresent());
//        //when
//        Session session = getSession();
//        session.beginTransaction();
//        session.delete(orders_productsEntity.get());
//        session.getTransaction().commit();
//        //then
//        final Query queryAfterDelete = getSession()
//                .createSQLQuery("SELECT * FROM orders_products WHERE order_id=" + 24).addEntity(OrdersProductsEntity.class);
//        Optional<OrdersProductsEntity> orders_productsEntityAfterDelete = (Optional<OrdersProductsEntity>) queryAfterDelete.uniqueResultOptional();
//        Assertions.assertFalse(orders_productsEntityAfterDelete.isPresent());
//    }
    @Test
    @Order(4)
    void addOrders_products_whenNotValid_shouldThrow() {
        //given
        OrdersProductsEntity entity = new OrdersProductsEntity();
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        //then
        Assertions.assertThrows(PersistenceException.class, () -> session.getTransaction().commit());
        ;
    }
}

