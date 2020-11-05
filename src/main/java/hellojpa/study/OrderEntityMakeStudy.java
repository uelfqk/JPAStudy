package hellojpa.study;

import Enums.OrderStatus;
import domain.Items.Book;
import domain.Order;
import domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OrderEntityMakeStudy {

    public void OrderEntityMake() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {


            Book book1 = new Book();
            book1.setName("바람");
            book1.setIsbn("바람isbn");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("바람");
            book2.setIsbn("바람isbn");
            book2.setPrice(10000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Order order = new Order();
            order.setStatus(OrderStatus.ORDER);
            em.persist(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setCount(10);
            orderItem.setItem(book1);
            orderItem.setOrder(order);
            em.persist(orderItem);

            OrderItem orderItem1 = new OrderItem();
            orderItem1.setCount(5);
            orderItem1.setItem(book2);
            orderItem1.setOrder(order);
            em.persist(orderItem1);





//            Order order = new Order();

//            order.setOrderItems();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
