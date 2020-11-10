package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NamedQueryStudy {

    public void NameQueryStudyInit() {
        // Named Query
        // 미리 정의해서 사용하는 JPQL
        // 동적 쿼리 -> 문자 더하기가 불가능함
        // 애노테이션, XML에 정의 -> XML이 우선권을 가진다.
        // 애플리케이션 로딩 시점에 JPA가 SQL로 파싱하여 초기화 후 캐싱하고 있다. -> 재사용 가능
        // 애플리케이션 로딩 시점에 쿼리를 검증 -> 가장 큰 메리트
        //  --> NameQuery에 문법 오류가 있으면 컴파일 타임에 오류 발생
        //
        // Spring Data JPA를 사용할때 인터페이스 메서드 위에 설정 가능 -> @Query
        // @Query(select m from Member m where m.name = :name)
        // void findByName(String name);
    }

    public void NameQuery() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
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

///* Member.findByUserName */ select
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
//            member0_.name=?
//          result :
//            member = Member{id=10, name='user1', address'null'}
//
//            select * from Member m where m.name = ?
            List<Member> resultMemberNameQuery = em.createNamedQuery("Member.findByUserName", Member.class)
                    .setParameter("name", "user1")
                    .getResultList();

            for (Member member : resultMemberNameQuery) {
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
