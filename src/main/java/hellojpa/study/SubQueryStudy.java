package hellojpa.study;

import domain.Member;
import domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SubQueryStudy {
    public void SubQuery() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("memberA");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // Exists : 서브쿼리에 결과가 존재하면 참
            // ALL : 모두 만족하면 참
            // ANY, SOME : 하나라도 만족하면 참참
            // IN : 서브쿼리의 결과중 하나라도 같은것이 있으면 참

//    /* select
//        m
//    from
//        Member m
//    where
//        exists (
//            select
//                t
//            from
//                m.team t
//            where
//                t.name = 'TeamA'
//        ) */ select
//            member0_.member_id as member_i1_5_,
//                    member0_.createBy as createBy2_5_,
//            member0_.createDate as createDa3_5_,
//                    member0_.lastModifyBy as lastModi4_5_,
//            member0_.lastModifyDate as lastModi5_5_,
//                    member0_.city as city6_5_,
//            member0_.street as street7_5_,
//                    member0_.zipcode as zipcode8_5_,
//            member0_.name as name9_5_,
//                    member0_.team_id as team_id12_5_,
//            member0_.endDate as endDate10_5_,
//                    member0_.startDate as startDa11_5_
//            from
//            Member member0_
//            where
//            exists (
//                    select
//                    team1_.team_id
//                    from
//                    Team team1_
//                    where
//                    member0_.team_id=team1_.team_id
//                    and team1_.name='TeamA'
//            )
            // Exists 예제
            // select * from Member m where exists (select * from Team t where t.name = 'TeamA')
            em.createQuery("select m from Member m where exists (select t from m.team t where t.name = 'TeamA')")
                    .getResultList();
//    /* select
//        o
//    from
//
//    Order o where
//        o.orderAmount > ALL (
//            select
//                p.stockAmount
//            from
//                Product p
//        ) */ select
//            order0_.order_id as order_id1_7_,
//                    order0_.createBy as createBy2_7_,
//            order0_.createDate as createDa3_7_,
//                    order0_.lastModifyBy as lastModi4_7_,
//            order0_.lastModifyDate as lastModi5_7_,
//                    order0_.delivery_id as delivery9_7_,
//            order0_.member_id as member_10_7_,
//                    order0_.orderAmount as orderAmo6_7_,
//            order0_.orderDate as orderDat7_7_,
//                    order0_.product_id as product11_7_,
//            order0_.status as status8_7_
//                    from
//            orders order0_
//            where
//            order0_.orderAmount>all (
//                    select
//                    product1_.stockAmount
//                    from
//                    Product product1_
//            )
            // ALL 예제
            // select * from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)
            em.createQuery("select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)")
                    .getResultList();

            // JPA는 From 절의 서브쿼리를 지원하지 않음 => 1. Join으로 풀어내거나 NativeQuery를 사용
            //                                       2. 애플리케이션 단으로 데이터를 가져와서 조립하는 형태로 만들어야함
            // From절의 서브쿼리 => InlineView => 가상의 테이블을 만들어 데이터를 정리한 뒤 사용하는 형태
            //em.createQuery("select mm.name, mm.age from (select m.name, m.age from Member m) as mm")
            //        .getResultList();

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
