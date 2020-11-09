package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntityDirectUseStudy {

    public void entityDirectUseInit() {
        // Init
        // JPQL에서 엔티티 직접 사용 - 기본 키 값
        // JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용
        // JPQL : select count(m) from Member m; m -> m.id
        // JPQL : select count(m.id) from Member m;
        // SQL  : select count(m.member_id) from Member m;
        // 엔티티로 기입하면 엔티티의 기본 키 값 (PK)를 사용
        // 엔티티의 외래키 값을 사용할 수 있음
        // Ex) select m from Member m where m.team = ?
        // m.team 은 실제 m.team.id
    }

    public void entityDirectUse() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
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

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member memberRoot = new Member();
            memberRoot.setName("root");
            memberRoot.setAge(29);
            memberRoot.changeTeam(teamA);
            em.persist(memberRoot);

            Member memberUser = new Member();
            memberUser.setName("user1");
            memberUser.setAge(20);
            memberUser.changeTeam(teamB);
            em.persist(memberUser);

            Member memberUser2 = new Member();
            memberUser2.setName("user2");
            memberUser2.setAge(19);
            memberUser2.changeTeam(teamA);
            em.persist(memberUser2);

            em.flush();
            em.clear();



///* select
//        m
//    from
//        Member m
//    where
//        m = :m */ select
//            member0_.member_id as member_i1_5_,
//                    member0_.createBy as createBy2_5_,
//            member0_.createDate as createDa3_5_,
//                    member0_.lastModifyBy as lastModi4_5_,
//            member0_.lastModifyDate as lastModi5_5_,
//                    member0_.city as city6_5_,
//            member0_.street as street7_5_,
//                    member0_.zipcode as zipcode8_5_,
//            member0_.age as age9_5_,
//                    member0_.name as name10_5_,
//            member0_.team_id as team_id13_5_,
//                    member0_.endDate as endDate11_5_,
//            member0_.startDate as startDa12_5_
//                    from
//            Member member0_
//            where
//            member0_.member_id=?
//          result :
//            member = Member{id=10, name='user1', address'null'}
//
//            select * from Member m where m.id = ?
            List<Member> resultMemberPK = em.createQuery("select m from Member m where m = :m", Member.class)
                    .setParameter("m", memberUser)
                    .getResultList();

            for (Member member : resultMemberPK) {
                System.out.println("member = " + member);
            }

            em.clear();

            List<Member> resultMemberFK = em.createQuery("select m from Member m where m.team = :team", Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : resultMemberFK) {
                System.out.println("member = " + member);
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
