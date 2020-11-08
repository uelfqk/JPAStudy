package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinStudy {

    public void fetchJoinInit() {
        // 페치 조인 (join fetch)
        // SQL 조인 종류 X
        // JPQL에서 성능 최적화를 위해 제공하는 전용 기능
        // 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
        // join fetch 명령어 사용
        // 페치 조인 - left join fetch(외부조인), right join fetch(외부조인), join fetch(내부조인)

        // 페치 조인과 조인의 차이
        // 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
        // JQPL : select t from Team t join t.members m where t.name = 'TeamA'
        // SQL : select * from Team t inner join Member m on t.team_id = m.team_id where t.name = 'TeamA'
        // 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시로딩)
        // 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념

        // JPQL은 결과를 반환할때 연관관계를 고려하지 않음
        // 단지 select 절에 지정한 엔티티만 조회할 뿐
        // 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회하지 않음
    }

    public void fetchJoinStart() {
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
///* select
//        m
//    from
//        Member m
//    join
//        fetch m.team */ select
//            member0_.member_id as member_i1_5_0_,
//                    team1_.team_id as team_id1_10_1_,
//            member0_.createBy as createBy2_5_0_,
//                    member0_.createDate as createDa3_5_0_,
//            member0_.lastModifyBy as lastModi4_5_0_,
//                    member0_.lastModifyDate as lastModi5_5_0_,
//            member0_.city as city6_5_0_,
//                    member0_.street as street7_5_0_,
//            member0_.zipcode as zipcode8_5_0_,
//                    member0_.age as age9_5_0_,
//            member0_.name as name10_5_0_,
//                    member0_.team_id as team_id13_5_0_,
//            member0_.endDate as endDate11_5_0_,
//                    member0_.startDate as startDa12_5_0_,
//            team1_.name as name2_10_1_
//                    from
//            Member member0_
//            inner join
//            Team team1_
//            on member0_.team_id=team1_.team_id
//          result :
//            member = Member{id=9, name='root', address'null'}
//            member = Member{id=10, name='user1', address'null'}
//            member = Member{id=11, name='user2', address'null'}
//
//            회원을 조회할대 팀의 정보까지 함께 조회
//            select * from Member m join Team t on m.team_id = t.team_id
            List<Member> resultFetchJoin = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();

            for (Member member : resultFetchJoin) {
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

    public void fetchJoinNpuls1Problem() {
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

///* select
//        m
//    from
//        Member m */ select
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
//            Hibernate:
//            select
//            team0_.team_id as team_id1_10_0_,
//                    team0_.name as name2_10_0_
//            from
//            Team team0_
//            where
//            team0_.team_id=?
//          result :
//            member.getTeam().getName() = TeamA
//            Hibernate:
//            select
//            team0_.team_id as team_id1_10_0_,
//                    team0_.name as name2_10_0_
//            from
//            Team team0_
//            where
//            team0_.team_id=?
//          result :
//            member.getTeam().getName() = TeamB
//            member.getTeam().getName() = TeamA
//
//            Member 엔티티만 탐색하여 가져온 결과
//            Member : 엔티티
//            Team : 프록시
//            member.getTeam().getName(); => 프록시 초기화 발생
//            Team의 데이터를 얻기위해 영속성 컨텍스트에 Team 데이터 요청
//            영속성 컨텍스트의 1차 캐시에 Team에 대한 데이터가 없음으로 Database에 Team 정보를 요청
//            이로 인해서 select Query가 2번 더 발생 => N + 1 문제 발생
//            로딩 전략을 LAZY(지연로딩), EAGER(즉시로딩)으로 설정하여도 N + 1 문제 발생
//            select * from Member m
//            select * from Team t where t.id = ? -> TeamA 조회
//            select * from Team t where t.id = ? -> TeamB 조회
            List<Member> resultMember = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            System.out.println("resultMember.size() = " + resultMember.size());

            for (Member member : resultMember) {
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
                // member[0] => getTeam().getName()[TeamA] => 영속성 컨텍스트 1차 캐시에 TeamA 데이터 없음 => Database에 select Query 발생
                // member[1] => getTeam().getName()[TeamB] => 영속성 컨텍스트 1차 캐시에 TeamB 데이터 없음 => Database에 select Query 발생
                // member[3] => getTeam().getName()[TeamA] => 영속성 컨텍스트 1차 캐시에 TeamA Entity 반환
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

    public void fetchJoinNplus1ProblemClear1() {
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
///* select
//        t
//    from
//        Team t
//    join
//        fetch t.members */ select
//            team0_.team_id as team_id1_10_0_,
//                    members1_.member_id as member_i1_5_1_,
//            team0_.name as name2_10_0_,
//                    members1_.createBy as createBy2_5_1_,
//            members1_.createDate as createDa3_5_1_,
//                    members1_.lastModifyBy as lastModi4_5_1_,
//            members1_.lastModifyDate as lastModi5_5_1_,
//                    members1_.city as city6_5_1_,
//            members1_.street as street7_5_1_,
//                    members1_.zipcode as zipcode8_5_1_,
//            members1_.age as age9_5_1_,
//                    members1_.name as name10_5_1_,
//            members1_.team_id as team_id13_5_1_,
//                    members1_.endDate as endDate11_5_1_,
//            members1_.startDate as startDa12_5_1_,
//                    members1_.team_id as team_id13_5_0__,
//            members1_.member_id as member_i1_5_0__
//                    from
//            Team team0_
//            inner join
//            Member members1_
//            on team0_.team_id=members1_.team_id
//            team.getName() = TeamA
//                    --> member = Member{id=9, name='root', address'null'}
//                    --> member = Member{id=11, name='user2', address'null'}
//            team.getName() = TeamB
//                    --> member = Member{id=10, name='user1', address'null'}
//            team.getName() = TeamA
//                    --> member = Member{id=9, name='root', address'null'}
//                    --> member = Member{id=11, name='user2', address'null'}
//
//            일대다 관계에서 fetch join을 사용하면 데이터 뻥튀기가 발생한다.
//            TeamA의 입장에서는 Member가 2명이 되니 row 가 2개가 된다. (데이터 뻥튀기)
//            (row1) 0x100
//            (row2) 0x100 -> TeamA(0x100) -> members -> 회원1, 회원2
//            select * from Team t inner join Member m on t.team_id = m.team_id
            List<Team> resultTeamFetchJoin = em.createQuery("select t from Team t join fetch t.members", Team.class)
                    .getResultList();

            for (Team team : resultTeamFetchJoin) {
                System.out.println("team.getName() = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("--> member = " + member);
                }
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

    // 일대다 관계에서 fetch join 시 중복제거
    public void fetchJoinUseDistinct() {
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
///* select
//        distinct t
//    from
//        Team t
//    join
//        fetch t.members */ select
//            distinct team0_.team_id as team_id1_10_0_,
//                    members1_.member_id as member_i1_5_1_,
//            team0_.name as name2_10_0_,
//                    members1_.createBy as createBy2_5_1_,
//            members1_.createDate as createDa3_5_1_,
//                    members1_.lastModifyBy as lastModi4_5_1_,
//            members1_.lastModifyDate as lastModi5_5_1_,
//                    members1_.city as city6_5_1_,
//            members1_.street as street7_5_1_,
//                    members1_.zipcode as zipcode8_5_1_,
//            members1_.age as age9_5_1_,
//                    members1_.name as name10_5_1_,
//            members1_.team_id as team_id13_5_1_,
//                    members1_.endDate as endDate11_5_1_,
//            members1_.startDate as startDa12_5_1_,
//                    members1_.team_id as team_id13_5_0__,
//            members1_.member_id as member_i1_5_0__
//                    from
//            Team team0_
//            inner join
//            Member members1_
//            on team0_.team_id=members1_.team_id
//          result :
//            resultTeamFetchJoinDistinct.size() = 2
//          result :
//            team.getName() = TeamA
//            member = Member{id=9, name='root', address'null'}
//            member = Member{id=11, name='user2', address'null'}
//            team.getName() = TeamB
//            member = Member{id=10, name='user1', address'null'}
//            중복 제거 (distinct)
//            SQL distinct : 중복 제거 => 이때 Member의 데이터가 2개이고 Member의 데이터가 정확히 동일한 데이터가 아니기 때문에 중복제거 불가
//            JPQL distinct : 애플리케이션 레벨에서 동일한 엔티티 중복 제거
//            sleect
            List<Team> resultTeamFetchJoinDistinct = em.createQuery("select distinct t from Team t join fetch t.members", Team.class)
                    .getResultList();

            System.out.println("resultTeamFetchJoinDistinct.size() = " + resultTeamFetchJoinDistinct.size());

            for (Team team : resultTeamFetchJoinDistinct) {
                System.out.println("team.getName() = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
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

    // SQL 조인과 JPQL 페치 조인 비교
    public void fetchJoinvsSqlJoinCompare() {
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
///* select
//        t
//    from
//        Team t
//    join
//        t.members */ select
//            team0_.team_id as team_id1_10_,
//                    team0_.name as name2_10_
//            from
//            Team team0_
//            inner join
//            Member members1_
//            on team0_.team_id=members1_.team_id
//            resultTeamSqlJoin.size() = 3
//            team.getName() = TeamA
//            Hibernate:
//            select
//            members0_.team_id as team_id13_5_0_,
//                    members0_.member_id as member_i1_5_0_,
//            members0_.member_id as member_i1_5_1_,
//                    members0_.createBy as createBy2_5_1_,
//            members0_.createDate as createDa3_5_1_,
//                    members0_.lastModifyBy as lastModi4_5_1_,
//            members0_.lastModifyDate as lastModi5_5_1_,
//                    members0_.city as city6_5_1_,
//            members0_.street as street7_5_1_,
//                    members0_.zipcode as zipcode8_5_1_,
//            members0_.age as age9_5_1_,
//                    members0_.name as name10_5_1_,
//            members0_.team_id as team_id13_5_1_,
//                    members0_.endDate as endDate11_5_1_,
//            members0_.startDate as startDa12_5_1_
//                    from
//            Member members0_
//            where
//            members0_.team_id=?
//            member = Member{id=9, name='root', address'null'}
//            member = Member{id=11, name='user2', address'null'}
//            team.getName() = TeamA
//            member = Member{id=9, name='root', address'null'}
//            member = Member{id=11, name='user2', address'null'}
//            team.getName() = TeamB
//            Hibernate:
//            select
//            members0_.team_id as team_id13_5_0_,
//                    members0_.member_id as member_i1_5_0_,
//            members0_.member_id as member_i1_5_1_,
//                    members0_.createBy as createBy2_5_1_,
//            members0_.createDate as createDa3_5_1_,
//                    members0_.lastModifyBy as lastModi4_5_1_,
//            members0_.lastModifyDate as lastModi5_5_1_,
//                    members0_.city as city6_5_1_,
//            members0_.street as street7_5_1_,
//                    members0_.zipcode as zipcode8_5_1_,
//            members0_.age as age9_5_1_,
//                    members0_.name as name10_5_1_,
//            members0_.team_id as team_id13_5_1_,
//                    members0_.endDate as endDate11_5_1_,
//            members0_.startDate as startDa12_5_1_
//                    from
//            Member members0_
//            where
//            members0_.team_id=?
//            member = Member{id=10, name='user1', address'null'}
            List<Team> resultTeamSqlJoin = em.createQuery("select t from Team t join t.members", Team.class)
                    .getResultList();

            System.out.println("resultTeamSqlJoin.size() = " + resultTeamSqlJoin.size());

            for (Team team : resultTeamSqlJoin) {
                System.out.println("team.getName() = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
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
