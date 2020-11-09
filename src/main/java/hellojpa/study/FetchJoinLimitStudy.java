package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinLimitStudy {
    public void fetchJoinLimitInit() {
        // Init
        // 페치 조인의 한계
        // 페치 조인 대상에는 별칭을 줄 수 없다.
        // 하이버네이트는 기능, 가급적 사용 하지 말라
        // 성능상 중간에 몇개를 걸러서 가져오고 싶다 -> 따로 조회하는것이 바람직하다.
        // Ex) Team과 관련된 회원이 5명인데 1명만 조회하면 나머지 4명이 누락되어있는 문제 발생
        //     1명만 따로 조작하는 것이 굉장히 위험하고 실무에서 장애를 일으킬 수 있는 확률이 올라간다.
        // JPA에서 의도한 설계는 team.getMembers() 할 때 전체 데이터를 다 가지고 있어야한다.
        // 객체 그래프라는것은 객체를 다 조회해야한다. 거르면서 조회하면 장애를 초래할 수 있다.
        // Ex) Team에서 Members를 100개 중 10개만 얻어오고 싶은 경우는
        // Team에서 조회하는것이 아니라 Member 객체를 10개 조회하는 JPQL을 작성해야한다.
        // A JPQL은 Member의 데이터를 10개만 조회하고
        // B JPQL은 Member의 데이터를 100개 조회했을때 영속성 컨텍스트는 어떻게 처리해야할지 모른다. -> 이런것들을 보장하지 않는다.
        // A에서 B를 가져오고 B에서 C를 조회할때는 별칭을 사용해도 된다.

        // 둘 이상의 컬렉션은 페치 조인 할 수 없다.
        // 컬렉션의 페치 조인은 하나만 지정할 수 있다.
        // Ex). Team 에서 Members를 조회하고 Orders를 조회하고
        //      이런 경우는 일 대 다 대 다 관계가 된다.
        // select t from Team t join fetch t.members -> 데이터 뻥튀기가 되는데
        // 두개 이상의 컬렉션을 페치 조인하면 어떤식으로 데이터 뻥튀기가 될지 모른다.

        // 페치 조인의 특징과 한계
        // 연관된 엔티티들을 SQL 한번으로 조회 - 성능 최적화
        // 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
        // ManyToOne(fetch = FetchType.LAZY) 이더라도 JPQL이 fetch join이 우선
        // 실무에서 글로벌 로딩전략은 지연로딩 설정
        // 최적화가 필요한 곳은 페치 조인 적용 -> N + 1 문제가 발생하는곳

        // 페치 조인 - 정리
        // 모든 것을 페치 조인으로 해결할 수 는 없음
        // 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
        // Ex) member.getTeam().getName()
        // 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야하면
        // 페치조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
        // 1. 엔티티를 페치조인으로 조회해서 그대로 쓴다.
        // 2. 페치 조인을 열심히 해서 애플리케이션 단에서 DTO로 조립한다.
        // 3. JPQL을 짤 때 new DTO를 사용해서 조립해서 데이터를 가져온다.

        // 페치 조인 중 컬렉션 값 연관 필드에서 페이징 API를 사용할 수 없다.
        // member1 -> TeamA
        // member2 -> TeamA
        // member3 -> TeamB
        // 아래와 같이 JPQL을 작성한 경우 데이터베이스의 Team 테이블의 데이터의 row는 2개이지만
        // 조회된 Team의 데이터는 3개가 되고 member1의 데이터만 가져오고 member2의 데이터는 누락되게 된다.
        // 하이버네이트는 경고를 남기고 메모리에서 페이징을 시도한다. -> WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
        // DB에서 모든 데이터를 퍼올린 다음에 메모리에서 페이징 처리
        // 실제 Paging Query가 DB로 나가지 않는다.
    }
    
    public void fetchJoinLimit() {
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

            // Init
            // 페치 조인의 한계
            // 페치 조인 대상에는 별칭을 줄 수 없다.
            // 하이버네이트는 기능, 가급적 사용 하지 말라
            // 성능상 중간에 몇개를 걸러서 가져오고 싶다 -> 따로 조회하는것이 바람직하다.
            // Ex) Team과 관련된 회원이 5명인데 1명만 조회하면 나머지 4명이 누락되어있는 문제 발생
            //     1명만 따로 조작하는 것이 굉장히 위험하고 실무에서 장애를 일으킬 수 있는 확률이 올라간다.
            // JPA에서 의도한 설계는 team.getMembers() 할 때 전체 데이터를 다 가지고 있어야한다.
            // 객체 그래프라는것은 객체를 다 조회해야한다. 거르면서 조회하면 장애를 초래할 수 있다.
            // Ex) Team에서 Members를 100개 중 10개만 얻어오고 싶은 경우는
            // Team에서 조회하는것이 아니라 Member 객체를 10개 조회하는 JPQL을 작성해야한다.
            // A JPQL은 Member의 데이터를 10개만 조회하고
            // B JPQL은 Member의 데이터를 100개 조회했을때 영속성 컨텍스트는 어떻게 처리해야할지 모른다. -> 이런것들을 보장하지 않는다.
            // A에서 B를 가져오고 B에서 C를 조회할때는 별칭을 사용해도 된다.

            // 둘 이상의 컬렉션은 페치 조인 할 수 없다.
            // 컬렉션의 페치 조인은 하나만 지정할 수 있다.
            // Ex). Team 에서 Members를 조회하고 Orders를 조회하고
            //      이런 경우는 일 대 다 대 다 관계가 된다.
            // select t from Team t join fetch t.members -> 데이터 뻥튀기가 되는데
            // 두개 이상의 컬렉션을 페치 조인하면 어떤식으로 데이터 뻥튀기가 될지 모른다.

            // 페치 조인의 특징과 한계
            // 연관된 엔티티들을 SQL 한번으로 조회 - 성능 최적화
            // 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
            // ManyToOne(fetch = FetchType.LAZY) 이더라도 JPQL이 fetch join이 우선
            // 실무에서 글로벌 로딩전략은 지연로딩 설정
            // 최적화가 필요한 곳은 페치 조인 적용 -> N + 1 문제가 발생하는곳

            // 페치 조인 - 정리
            // 모든 것을 페치 조인으로 해결할 수 는 없음
            // 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
            // Ex) member.getTeam().getName()
            // 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야하면
            // 페치조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
            // 1. 엔티티를 페치조인으로 조회해서 그대로 쓴다.
            // 2. 페치 조인을 열심히 해서 애플리케이션 단에서 DTO로 조립한다.
            // 3. JPQL을 짤 때 new DTO를 사용해서 조립해서 데이터를 가져온다.

            // 페치 조인 중 OneToMany 연관관계에서 페이징 API를 사용할 수 없다.
            // member1 -> TeamA
            // member2 -> TeamA
            // member3 -> TeamB
            // 아래와 같이 JPQL을 작성한 경우 데이터베이스의 Team 테이블의 데이터의 row는 2개이지만
            // 조회된 Team의 데이터는 3개가 되고 member1의 데이터만 가져오고 member2의 데이터는 누락되게 된다.
            // 하이버네이트는 경고를 남기고 메모리에서 페이징을 시도한다. -> WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
            // DB에서 모든 데이터를 퍼올린 다음에 메모리에서 페이징 처리
            // 실제 Paging Query가 DB로 나가지 않는다.
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
//
//            select * from Team t inner join Member m on t.team_id = m.team_id
            em.createQuery("select t from Team t join fetch t.members", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            List<Team> resultFetchJoinAlias = em.createQuery("select t from Team t join fetch t.members m", Team.class)
                    .getResultList();

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
//            on member0_.team_id=team1_.team_id limit ?
//          result :   
//            member.getTeam().getName() = TeamA
            // 처리하는 방법
            // JPQL의 주체를 변경한다. Team -> Member
            List<Member> resultMemberFetchJoinPaging = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            for (Member member : resultMemberFetchJoinPaging) {
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }

            em.clear();


///* select
//        t
//    from
//        Team t */ select
//            team0_.team_id as team_id1_10_,
//                    team0_.name as name2_10_
//            from
//            Team team0_ limit ?
//                    team.getName() = TeamA
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
//
//            Team만 조회할때 Paging을 사용하고 Member 엔티티를 사용할 때 초기화
//            Paging은 하였느나 N + 1 문제 발생
//            이를 해결하기 위해 Team 엔티티의 members 필드에 @BatchSize(size = 100) 사용
//            @BatchSize : 1000이하의 수중에 적당한 크기로 지정
//                         한번에 지정된 사이즈만큼 연관된 엔티티를 함께 조회 (데이터베이스에서 실제 데이터를 퍼올림)
//            또는 글로벌 세팅으로 resource.META-INF.persistence.xml ->
//                              <persistence-unit name="hello">.<properties>.<property name="hibernate.default_batch_fetch_size" value="100"/>
            List<Team> resultTeam = em.createQuery("select t from Team t", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : resultTeam) {
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

    public void fetchJoin() {
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
//
//            select * from Team t inner join Member m on t.team_id = m.team_id
            em.createQuery("select t from Team t join fetch t.members", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            List<Team> resultFetchJoinAlias = em.createQuery("select t from Team t join fetch t.members m", Team.class)
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

    public void fetchJoinLimitPaging1() {
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
//            on member0_.team_id=team1_.team_id limit ?
//          result :
//            member.getTeam().getName() = TeamA
            // 처리하는 방법
            // JPQL의 주체를 변경한다. Team -> Member
            List<Member> resultMemberFetchJoinPaging = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            for (Member member : resultMemberFetchJoinPaging) {
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }

            em.clear();


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    public void fetchJoinLimitPaging2() {
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
//        Team t */ select
//            team0_.team_id as team_id1_10_,
//                    team0_.name as name2_10_
//            from
//            Team team0_ limit ?
//                    team.getName() = TeamA
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
//
//            Team만 조회할때 Paging을 사용하고 Member 엔티티를 사용할 때 초기화
//            Paging은 하였느나 N + 1 문제 발생
//            이를 해결하기 위해 Team 엔티티의 members 필드에 @BatchSize(size = 100) 사용
//            @BatchSize : 1000이하의 수중에 적당한 크기로 지정
//                         한번에 지정된 사이즈만큼 연관된 엔티티를 함께 조회 (데이터베이스에서 실제 데이터를 퍼올림)
//            또는 글로벌 세팅으로 resource.META-INF.persistence.xml ->
//                              <persistence-unit name="hello">.<properties>.<property name="hibernate.default_batch_fetch_size" value="100"/>
            List<Team> resultTeam = em.createQuery("select t from Team t", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : resultTeam) {
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
