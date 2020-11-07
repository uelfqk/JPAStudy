package hellojpa;

import Enums.OrderStatus;
import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            // JPQL 기타식
            // SQL과 문법이 같은 식과 동일하게 지원
            // EXISTS, IN
            // AND, OR, NOT
            // =, >, >=, <, <=, <>
            // BETWEEN, LIKE, IS NULL
            Item item = new Book();
            item.setName("jaa jaql");
            item.setPrice(10000);
            item.setStockQuantity(100);
            em.persist(item);

            Book book = new Book();
            book.setName("jpa jpql");
            book.setPrice(10000);
            book.setStockQuantity(99);
            book.setAuthor("가나다라");
            book.setIsbn("isbn");
            em.persist(book);

            Address address = new Address("city", "street", "zipcode");

            Delivery delivery = new Delivery();
            delivery.setAddress(address);
            em.persist(delivery);

            Order order = new Order();
            order.setDelivery(delivery);
            order.setStatus(Enums.OrderStatus.ORDER);
            em.persist(order);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(1);
            orderItem.setOrder(order);
            em.persist(orderItem);

            OrderItem orderItem1 = new OrderItem();
            orderItem1.setItem(book);
            orderItem1.setCount(20);
            orderItem1.setOrder(order);
            em.persist(orderItem1);

            Member member = new Member();
            member.setName("root");
            em.persist(member);
            em.flush();
            em.clear();
            
///* select
//        i 
//    from
//        Item i 
//    where
//        i.stockQuantity between 99 and 100 */ select
//            item0_.item_id as item_id2_4_,
//                    item0_.createBy as createBy3_4_,
//            item0_.createDate as createDa4_4_,
//                    item0_.lastModifyBy as lastModi5_4_,
//            item0_.lastModifyDate as lastModi6_4_,
//                    item0_.name as name7_4_,
//            item0_.price as price8_4_,
//                    item0_.stockQuantity as stockQua9_4_,
//            item0_.author as author10_4_,
//                    item0_.isbn as isbn11_4_,
//            item0_.artist as artist12_4_,
//                    item0_.etc as etc13_4_,
//            item0_.actor as actor14_4_,
//                    item0_.director as directo15_4_,
//            item0_.DTYPE as DTYPE1_4_
//                    from
//            Item item0_
//            where
//            item0_.stockQuantity between 99 and 100
//            item1 = Book{author='null', isbn='null'}
//            item1 = Book{author='가나다라', isbn='isbn'}
//            Between 조건으로 탐색
            List<Item> resultItemBetween = em.createQuery("select i from Item i where i.stockQuantity between 99 and 100", Item.class)
                    .getResultList();

            for (Item item1 : resultItemBetween) {
                System.out.println("item1 = " + item1);
            }

///* select
//        i
//    from
//        Item i
//    where
//        i.name like '%jp%' */ select
//            item0_.item_id as item_id2_4_,
//                    item0_.createBy as createBy3_4_,
//            item0_.createDate as createDa4_4_,
//                    item0_.lastModifyBy as lastModi5_4_,
//            item0_.lastModifyDate as lastModi6_4_,
//                    item0_.name as name7_4_,
//            item0_.price as price8_4_,
//                    item0_.stockQuantity as stockQua9_4_,
//            item0_.author as author10_4_,
//                    item0_.isbn as isbn11_4_,
//            item0_.artist as artist12_4_,
//                    item0_.etc as etc13_4_,
//            item0_.actor as actor14_4_,
//                    item0_.director as directo15_4_,
//            item0_.DTYPE as DTYPE1_4_
//                    from
//            Item item0_
//            where
//            item0_.name like '%jp%'
//            item1 = Book{author='가나다라', isbn='isbn'}
//
//            like 조건으로 탐색
            List<Item> resultItemLike = em.createQuery("select i from Item i where i.name like '%jp%'", Item.class)
                    .getResultList();

            for (Item item1 : resultItemLike) {
                System.out.println("item1 = " + item1);
            }

///* select
//        b
//    from
//        Book b
//    where
//        b.isbn is null  */ select
//            book0_.item_id as item_id2_4_,
//                    book0_.createBy as createBy3_4_,
//            book0_.createDate as createDa4_4_,
//                    book0_.lastModifyBy as lastModi5_4_,
//            book0_.lastModifyDate as lastModi6_4_,
//                    book0_.name as name7_4_,
//            book0_.price as price8_4_,
//                    book0_.stockQuantity as stockQua9_4_,
//            book0_.author as author10_4_,
//                    book0_.isbn as isbn11_4_
//            from
//            Item book0_
//            where
//            book0_.DTYPE='Book'
//            and (
//                    book0_.isbn is null
//            )
//            book1 = Book{author='null', isbn='null'}
//
//            is null 조건으로 탐색
            List<Book> resultBookIsNull = em.createQuery("select b from Book b where b.isbn is null ", Book.class)
                    .getResultList();

            for (Book book1 : resultBookIsNull) {
                System.out.println("book1 = " + book1);
            }

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
