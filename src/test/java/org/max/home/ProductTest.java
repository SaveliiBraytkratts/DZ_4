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
public class ProductTest extends AbstractTest{

    @Test
    @Order(1)
    void getProduct_whenValid_shouldReturn () throws SQLException {
        //given
        String sql = "SELECT * FROM products";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery("SELECT * FROM products").addEntity(CustomersEntity.class);
        //then
        Assertions.assertEquals(10, countTableSize);

    }
    @Test
    @Order(2)
    void addProduct_whenValid_shouldSave() {
        //given
        ProductsEntity entity = new ProductsEntity();
        entity.setProductId((short) 11);
        entity.setPrice("100.0");
        entity.setMenuName("sausage");
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        final Query query = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id="+11).addEntity(ProductsEntity.class);
        ProductsEntity creditEntity = (ProductsEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(creditEntity);
        Assertions.assertEquals("100.0", creditEntity.getPrice());
    }
    @Test
    @Order(3)
    void deleteProduct_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id=" + 11).addEntity(ProductsEntity.class);
        Optional<ProductsEntity> productsEntity = (Optional<ProductsEntity>) query.uniqueResultOptional();
        Assumptions.assumeTrue(productsEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(productsEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id=" + 11).addEntity(ProductsEntity.class);
        Optional<ProductsEntity> productsEntityAfterDelete = (Optional<ProductsEntity>) queryAfterDelete.uniqueResultOptional();
        Assertions.assertFalse(productsEntityAfterDelete.isPresent());
    }
    @Test
    @Order(4)
    void addProduct_whenNotValid_shouldThrow() {
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