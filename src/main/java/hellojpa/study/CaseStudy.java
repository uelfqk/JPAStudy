package hellojpa.study;

import domain.*;
import domain.Items.Book;
import domain.Items.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CaseStudy {
    public void caseWhenThen() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            Member memberRoot = new Member();
            memberRoot.setName("root");
            memberRoot.setAge(29);
            em.persist(memberRoot);

            Member memberUser = new Member();
            memberUser.setName("member");
            memberUser.setAge(20);
            em.persist(memberUser);

            Member memberUser2 = new Member();
            memberUser2.setAge(19);
            em.persist(memberUser2);

            em.flush();
            em.clear();

///* select
//        case
//            when m.name = 'root' then '관리자'
//            else '일반 사용자'
//        end
//    from
//        Member m */ select
//            case
//                    when member0_.name='root' then '관리자'
//                else '일반 사용자'
//                end as col_0_0_
//                    from
//                Member member0_
//            result :
//                s = 관리자
//                s = 일반 사용자
//
//            기본 CASE 식 case when then 탐색쿼리에서 결과 얻기
//            select case m.name = 'root' then '관리자' else '일반사용자' end from Member m
            List<String> resultCase = em.createQuery("select " +
                    "case when m.name = 'root' then '관리자' " +
                    "else '일반 사용자' " +
                    "end " +
                    "from Member m", String.class)
                    .getResultList();

            for (String s : resultCase) {
                System.out.println("s = " + s);
            }

///* select
//        case m.name
//            when 'root' then '관리자'
//            else '일반 사용자'
//        end
//    from
//        Member m */ select
//            case member0_.name
//                when 'root' then '관리자'
//                else '일반 사용자'
//                end as col_0_0_
//                    from
//                Member member0_
//            result :
//                s = 관리자
//                s = 일반 사용자
//
//            단순 CASE식
//            select case m.name when
            em.createQuery("select " +
                    "case m.name when 'root' then '관리자' " +
                    "else '일반 사용자' " +
                    "end " +
                    "from Member m", String.class)
                    .getResultList();

            for (String s : resultCase) {
                System.out.println("s = " + s);
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

    public void coalesce() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            Member memberRoot = new Member();
            memberRoot.setName("root");
            memberRoot.setAge(29);
            em.persist(memberRoot);

            Member memberUser = new Member();
            memberUser.setName("member");
            memberUser.setAge(20);
            em.persist(memberUser);

            Member memberUser2 = new Member();
            memberUser2.setAge(19);
            em.persist(memberUser2);

            em.flush();
            em.clear();

///* select
//        coalesce(m.name,
//        '이름없는 회원')
//    from
//        Member m */ select
//            coalesce(member0_.name,
//                    '이름없는 회원') as col_0_0_
//            from
//            Member member0_
//          result :
//            s = root
//            s = member
//            s = 이름없는 회원
//
//            Coalesce : 해당 컬럼의 값이 NULL인 경우 Coalesce로 지정한 값으로 반환
//            select coalesce(m.name, '이름없는 회원') from Member m
            List<String> resultCoalesce = em.createQuery("select coalesce(m.name, '이름없는 회원') from Member m", String.class)
                    .getResultList();

            for (String s : resultCoalesce) {
                System.out.println("s = " + s);
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

    public void nullIf() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {
            Member memberRoot = new Member();
            memberRoot.setName("root");
            memberRoot.setAge(29);
            em.persist(memberRoot);

            Member memberUser = new Member();
            memberUser.setName("member");
            memberUser.setAge(20);
            em.persist(memberUser);

            Member memberUser2 = new Member();
            memberUser2.setAge(19);
            em.persist(memberUser2);

            em.flush();
            em.clear();

///* select
//        nullif(m.name,
//        'root')
//    from
//        Member m
//    where
//        m.name is not null */ select
//            nullif(member0_.name,
//                    'root') as col_0_0_
//            from
//            Member member0_
//            where
//            member0_.name is not null
//           result :
//            s = null
//            s = member
//
//            NULLIF : 해당 컬럼의 값이 지정한 값과 동일하면 null로 반환
//            사용 예 : 관리자의 정보를 숨긴다던지
//            해당 컬럼의 값이 null 이면 비교할 수 없음으로 NULL 반환
//            select nullif(m.name, 'root') from Member m
            List<String> resultNullif = em.createQuery("select nullif(m.name, 'root') from Member m " +
                    "where m.name is not null", String.class)
                    .getResultList();

            for (String s : resultNullif) {
                System.out.println("s = " + s);
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
