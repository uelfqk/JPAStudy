package hellojpa.study;

import Enums.OrderStatus;
import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlTypeorEtcStudy {
    public void jpqlType() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            // JPQL 타입 표현과 기타식
            // 문자 : 'Hello', 'She''s' // '' 안에 문자 입력
            // 숫자 : 10L(Long), 10D(Double), 10F(Float)
            // Boolean : TRUE, FALSE
            // ENUM : hellojpa.Eums.OrderStatus.ORDER
            Item item = new Book();
            item.setName("jpa jpql");
            item.setPrice(10000);
            item.setStockQuantity(100);
            em.persist(item);

            Book book = new Book();
            book.setName("jpa jpql");
            book.setPrice(10000);
            book.setStockQuantity(100);
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
//        m.name,
//        'hello',
//        true
//    from
//        Member m */ select
//            member0_.name as col_0_0_,
//                    'hello' as col_1_0_,
//            1 as col_2_0_
//            from
//            Member member0_
//            result :
//            objects[0] = root
//            objects[0] = hello
//            objects[0] = true
//
//            Process finished with exit code 0
//            Member Entity의 name과 문자 'hello'를 탐색
//            select m.name, 'hello', true from Member m
            List<Object[]> result = em.createQuery("select m.name, 'hello', true from Member m")
                    .getResultList();

            for (Object[] objects : result) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[0] = " + objects[1]);
                System.out.println("objects[0] = " + objects[2]);
            }

///* select
//    o.status
//from
//
//Order o where
//    o.status = :status */ select
//        order0_.status as col_0_0_
//                from
//        orders order0_
//        where
//        order0_.status=?
//        result :
//        orderStatus = ORDER
//
//            Enum Type 으로 탐색 SetParameter 사용
//            select o.status from Orders o where o.status = ORDER
            List<OrderStatus> resultOrderStatusUseSetParameter = em.createQuery("select o.status from Order o " +
                    "where o.status = :status", OrderStatus.class)
                    .setParameter("status", OrderStatus.ORDER)
                    .getResultList();

//            Enum Type 으로 탐색 Query 안에 Enum Type 명시
//            List<OrderStatus> resultOrderStatusQueryInEnumsType = em.createQuery("select o.status from Order o " +
//                    "where o.status = Enums.OrderStatus.ORDER", OrderStatus.class)
//                    .getResultList();
//
//            for (Object orderStatus : resultOrderStatusUseSetParameter) {
//                System.out.println("orderStatus = " + orderStatus);
//            }

///* select
//        i
//    from
//        Item i
//    where
//        type(i) = Book */ select
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
//            item0_.DTYPE='Book'
//            result :
//            item1 = Book{author='null', isbn='null'}
//            item1 = Book{author='가나다라', isbn='isbn'}
//            엔티티 타입 : Type(i) = BOOK
//            상속관계 매핑에서 사용 가능
//            부모 엔티티의 @DiscriminatorColumn 설정된 DTYPE으로 조건을 지정하여 탐색
//            select * from Item i where where i.DTYPE = 'BOOK'
            List<Item> resultItem = em.createQuery("select i from Item i where type(i) = Book", Item.class)
                    .getResultList();

            for (Item item1 : resultItem) {
                System.out.println("item1 = " + item1);
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

    public void jpqlEtc() {
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
//            result :
//            item1 = Book{author='null', isbn='null'}
//            item1 = Book{author='가나다라', isbn='isbn'}
//            Between 조건으로 탐색
//            select * from Item i where i.stockQuantity between 99 and 100
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
//            result :
//            item1 = Book{author='가나다라', isbn='isbn'}
//
//            like 조건으로 탐색
//            select * from Item i where i.name like '%jp%'
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
//            result :
//            book1 = Book{author='null', isbn='null'}
//
//            is null 조건으로 탐색
//            select * from Book b where b.isbn is null
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
