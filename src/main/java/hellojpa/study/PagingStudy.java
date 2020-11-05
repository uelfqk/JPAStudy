package hellojpa.study;

import domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class PagingStudy {

    public void Paging() {
        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        //aaaa
        tx.begin();

        try {

            /* select
        m
    from
        Member m
    order by
        m.id desc */
//            select
//            member0_.member_id as member_i1_4_,
//                    member0_.createBy as createBy2_4_,
//            member0_.createDate as createDa3_4_,
//                    member0_.lastModifyBy as lastModi4_4_,
//            member0_.lastModifyDate as lastModi5_4_,
//                    member0_.city as city6_4_,
//            member0_.street as street7_4_,
//                    member0_.zipcode as zipcode8_4_,
//            member0_.name as name9_4_,
//                    member0_.endDate as endDate10_4_,
//            member0_.startDate as startDa11_4_
//                    from
//            Member member0_
//            order by
//            member0_.member_id desc limit ? offset ?
//
//            select * from Member order by member_id desc limit 1 offset 30
            List<Member> result = em.createQuery("select m from Member m order by m.id desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(30)
                    .getResultList();

            for (Member member : result) {
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
