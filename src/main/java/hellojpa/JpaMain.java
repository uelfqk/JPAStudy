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
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("memberA");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();
//    /* select
//        m
//    from
//        Member m
//    join
//        m.team t */ select
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
//            inner join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//
//            내부 조인 (inner join)
//            select * from Member m inner join Team t on m.id = t.id
            em.createQuery("select m from Member m join m.team t")
                    .getResultList();
///* select
//        m
//    from
//        Member m
//    left outer join
//        m.team t */ select
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
//            left outer join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//
//            외부조인 (left outer join 왼쪽 테이블을 기준으로 조인하여 검색)
//            select * from Member m left outer join Team t on m.id = t.id
            em.createQuery("select m from Member m left outer join m.team t")
                    .getResultList();
//    /* select
//        m
//    from
//        Member m
//    right outer join
//        m.team */ select
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
//            right outer join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//
//            외부조인 (left outer join 오른쪽 테이블을 기준으로 조인)
//            select * from Member m right outer join Team t on m.id = t.id
            em.createQuery("select m from Member m right outer join m.team")
                    .getResultList();

//    /* select
//        m
//    from
//        Member m,
//        Team t
//    where
//        m.name = t.name */ select
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
//            Member member0_ cross
//                    join
//            Team team1_
//            where
//            member0_.name=team1_.name
//            11월 06, 2020 12:24:35 오전 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl stop
//            INFO: HHH10001008: Cleaning up connection pool [jdbc:h2:tcp://localhost/~/test]
//
//            Process finished with exit code 0
//
//            세타(cross)조인 (테이블의 모든 레코드를 곱집합으로 만든 뒤 조건에 맞는 데이터 출력)
//            select * from Member m, Team t where m.name = t.name
            em.createQuery("select m from Member m, Team t where m.name = t.name")
                    .getResultList();

//            조인 - ON절
//            조인할때 조인대상을 필터링 할 수 있다.
//            연관관계 없는 엔티티 외부조인 가능 하이버네이트 5.1부터 사용 가능
//            예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
///* select
//        m
//    from
//        Member m
//    left join
//        m.team t
//            on t.name = 'A' */ select
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
//            left outer join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//            and (
//                    team1_.name='A'
//            )
//          left join 의 on 절을 사용하여 팀이름이 A인 데이터를 탐색
            em.createQuery("select m from Member m left join m.team t on t.name = 'A'")
                    .getResultList();


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
