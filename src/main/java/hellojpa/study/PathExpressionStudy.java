package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class PathExpressionStudy {
    public void PathExpression() {
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

            // 경로식 표현 -> .(점)을 찍어 객체 그래프를 탐색하는 것
            // select m.name -> 상태 필드 *
            // from Member m
            //  join m.team t -> 단일 값 연관 필드 *
            //  join m.orders o -> 컬렉션 값 연관 필드 *
            // where t.name = 'TeamA'
            // 경로 표현식에 따라 결과값이 달라진다.

///* select
//        m.name
//    from
//        Member m */ select
//            member0_.name as col_0_0_
//                    from
//            Member member0_
//           result :
//            s = root
//            s = member
//            s = null
//            상태 필드 (state Field) -> 경로 탐색 끝, 그래프 탐색 X
            List<String> resultName = em.createQuery("select m.name from Member m", String.class)
                    .getResultList();

            for (String s : resultName) {
                System.out.println("s = " + s);
            }

///* select
//        m.team
//    from
//        Member m */ select
//            team1_.team_id as team_id1_10_,
//                    team1_.name as name2_10_
//            from
//            Member member0_
//            inner join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//        result :
//            team.getName() = TeamA
//            team.getName() = TeamA
//            team.getName() = TeamB
//
//            단일 값 연관 경로 -> 묵시적 내부 조인 발생, 탐색 가능
//            단일 값 연관 경로에서 JPQL과 SQL를 서로 맞추는것이 유지보수와 튜닝에 좋다
//            묵시적으로 내부 조인이 발생하기 때문
            List<Team> resultTeam = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            for (Team team : resultTeam) {
                System.out.println("team.getName() = " + team.getName());
            }


///* select
//        t.members
//    from
//        Team t */ select
//            members1_.member_id as member_i1_5_,
//                    members1_.createBy as createBy2_5_,
//            members1_.createDate as createDa3_5_,
//                    members1_.lastModifyBy as lastModi4_5_,
//            members1_.lastModifyDate as lastModi5_5_,
//                    members1_.city as city6_5_,
//            members1_.street as street7_5_,
//                    members1_.zipcode as zipcode8_5_,
//            members1_.age as age9_5_,
//                    members1_.name as name10_5_,
//            members1_.team_id as team_id13_5_,
//                    members1_.endDate as endDate11_5_,
//            members1_.startDate as startDa12_5_
//                    from
//            Team team0_
//            inner join
//            Member members1_
//            on team0_.team_id=members1_.team_id
//          result :
//            resultMember = Member{id=9, name='root', address'null'}
//            resultMember = Member{id=10, name='user1', address'null'}
//            resultMember = Member{id=11, name='user2', address'null'}
//
//            Collection members에서 탐색할 수 없다.
//            Collection에 값들이 담겨있기 때문
//            select t.members. -> 사용 불가
//            select t.members.size -> 사용 가능
//            묵시적 조인을 사용하지 말라
//            select * from Team t inner join Member m on t.team_id = m.team_id
            Collection resultMembers = em.createQuery("select t.members from Team t", Collection.class)
                    .getResultList();

            for (Object resultMember : resultMembers) {
                System.out.println("resultMember = " + resultMember);
            }

///* select
//        m.name
//    from
//        Team t
//    join
//        t.members m */ select
//            members1_.name as col_0_0_
//                    from
//            Team team0_
//            inner join
//            Member members1_
//            on team0_.team_id=members1_.team_id
//          result :
//            s = root
//            s = user1
//            s = user2
//
//            명시적 조인을 사용하면 엘리어스로 별칭을 받아 새로 시작할 수 있다.
//            select m.name from Team t inner join Member m on t.team_id = m.team_id
            List<String> resultMemberName = em.createQuery("select m.name from Team t join t.members m", String.class)
                    .getResultList();

            for (String s : resultMemberName) {
                System.out.println("s = " + s);
            }

///* select
//        t.members.size
//    from
//        Team t */ select
//                    (select
//                            count(members1_.team_id)
//                            from
//                            Member members1_
//                            where
//                            team0_.team_id=members1_.team_id) as col_0_0_
//            from
//            Team team0_
//          result :
//            integer = 2
//            integer = 1
//
//            컬랙션 값 연관 경로를 사용했을때 size는 가져올 수 있다.
//            select (select count(m.team_id) from Member m where t.team_id = m.team_id) from Team t
            List<Integer> resultCollectionSize = em.createQuery("select t.members.size from Team t", Integer.class)
                    .getResultList();

            for (Integer integer : resultCollectionSize) {
                System.out.println("integer = " + integer);
            }

//            select o.members.team from Order o ---------> 성공 : 묵시적 조인 2번 발생
//            select t.members from Team t ---------------> 성공 : 묵시적 조인이 발생하지만 members에서 더 이상 탐색하지 않기떄문
//            select t.members.name from Team t ----------> 실패 : 컬렉션에서 더 탐색을 하기 떄문
//            select m.name from Team t join t.members m -> 성공 : 묵시적 조인이 발생하지만 별칭을 통해 다시 시작
//            묵시적 조인은 내부조인만 가능
//            컬랙션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야 탐색 가능
//            경로 탐색은 주로 select, where 절에서 사용하지만 묵시적 조인으로 인해 from (join)절에 영향을 줌ㅎ
            //System.out.println("singleResultCollectionSize = " + singleResultCollectionSize);


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
