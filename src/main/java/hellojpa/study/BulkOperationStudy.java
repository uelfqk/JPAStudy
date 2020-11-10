package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BulkOperationStudy {

    public void BulkOperationStudyInit() {
        // 벌크 연산
        // Ex) 재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면?
        // JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
        //  1. 재고가 10개 미만인 상품을 리스트로 조회
        //  2. 상품 엔티티의 가격을 10% 증가
        //  3. 트렌젝션 커밋 시점에 변경감지 작동
        // 조회한 상품의 수량이 100만건이이라면 Update SQL 100만건 실행
        //
        // JPA는 벌크성 보다는 실시간 단건 단건에 더 최적화가 되어있다.
        // 하지만 현실적으로 벌크연산을 안할 수 없으니 기능을 제공한다.
        // 쿼리 한번으로 여러 테이블의 행(row)를 변경
        //
        // Spring Data JPA에서 @Modify 를 사용하면 벌크연산 가능
        // @Modify 으로 벌크 연산 SQL 실행 후 자동으로 영속성 컨텍스트를 자동으로 Clear
    }

    public void BulkOperation() {
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

//            member = Member{id=9, name='root', age='29', address'null'}
//            member = Member{id=10, name='user1', age='20', address'null'}
//            member = Member{id=11, name='user2', age='19', address'null'}
            List<Member> resultMemberBefore = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member : resultMemberBefore) {
                System.out.println("member = " + member);
            }

///* update
//        Member m
//    set
//        m.age = 30 */ update
//                    Member
//            set
//                    age=30
//            resultCount = 3
//
            int resultCount = em.createQuery("update Member m set m.age = 30")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            // 벌크 연산 이후 영속성 컨텍스트에 남아있는 값은 DB에서 새로 가져온 엔티티가 아닌
            // 이전에 조회된 엔티티임으로 이전 값이 그대로 남아있다.
            // 그렇기 때문에 벌크 연산을 실행 한 뒤에는 영속성 컨텍스트를 초기화해주고 다시 조회하는것이
            // 벌크 연산으로 변경된 엔티티를 가질 수 있다.
            // 영속성 컨텍스트를 초기화 하지 않으면 처음에 조회한 회원의 나이가 20살이고 벌크 연산을 통해 30살로 변경해도
            // 다음에 다시 조회할때 영속성 컨텍스트에서 엔티티를 가져오기 때문에 반환되는 결과는 회원의 나이가 20살인 엔티티를 얻게 된다.
            // 이 부분만 주의하면 된다. (데이터 정합성이 맞지 않음)
            em.clear();

            // 영속성 컨텍스트를 초기화 하지 않고 Member 엔티티를 조회한 결과
//            member = Member{id=9, name='root', age='29', address'null'}
//            member = Member{id=10, name='user1', age='20', address'null'}
//            member = Member{id=11, name='user2', age='19', address'null'}

            // 영속성 컨텍스트 초기화 후 Member 엔티티를 조회한 결과
//            member = Member{id=9, name='root', age='30', address'null'}
//            member = Member{id=10, name='user1', age='30', address'null'}
//            member = Member{id=11, name='user2', age='30', address'null'}
            List<Member> resultMemberAfter = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member : resultMemberAfter) {
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
