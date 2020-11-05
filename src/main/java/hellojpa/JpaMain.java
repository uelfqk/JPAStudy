package hellojpa;

import domain.Address;
import domain.Member;
import domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setName("member" + i);
                Address address = new Address();
                address.setCity("Incheon");
                address.setStreet("Namdong");
                address.setZipcode("ci" + i);
                member.setAddress(address);
                em.persist(member);
            }

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

//            Movie movie = new Movie();
//            movie.setName("바람");
//            movie.setPrice(1000);
//            movie.setActor("aaa");
//            movie.setDirector("bbb");
//            movie.setRegistDateTime(LocalDateTime.now());
//            movie.setUpdateDateTime(LocalDateTime.now());
//            em.persist(movie);
//
//            em.flush();
//            em.clear();
//
//            Movie findItem = em.find(Movie.class, movie.getId());
//
//            findItem.setName("람바");
//            findItem.setUpdateDateTime(LocalDateTime.now());


//            Book book = new Book();
//            book.setName("adada");
//            book.setPrice(10000);
//            book.setAuthor("가나타라라");
//            book.setIsbn("아이에스비엔");
////            em.persist(book);
//            em.flush();
//            em.clear();

//            Movie movie = new Movie();
//            movie.setName("aaa");
//            movie.setPrice(10);
//            movie.setActor("Actor");
//            movie.setDirector("카카카ㅏ");
//            em.persist(movie);

            //Movie movie1 = em.find(Movie.class, movie.getId());

            // 아 진짜 뭐지 기분이 안좋은 느낌이야...
            //

            // 조인 전략
//            Book book = new Book();
//            book.setAuthor("sss");
//            book.setIsbn("isbn");
//            book.setName("JPA");
//            book.setPrice(10000);
//            em.persist(book);
//
//            Album album = new Album();
//            album.setArtist("노라조");
//            album.setName("rrr");
//            album.setPrice(20000);
//            em.persist(album);
//
//            em.flush();
//            em.clear();
//
//            em.find(Book.class, book.getId());
//            em.find(Album.class, album.getId());

//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("root");
//            member.changeTeam(team);
//            em.persist(member);
//
//
//
//            System.out.println("==================================");
//
////            em.flush();
////            em.clear();
//
//            Member findMember = em.find(Member.class, member.getId());
//            System.out.println("findMember.getClass() = " + findMember.getClass());
//            System.out.println("findMember.getTeam().Class() = " + findMember.getTeam().getClass());
//            System.out.println("==================================");
//
//            Team findTeam = member.getTeam();
//
//            for(Member m1 : findMember.getTeam().getMembers()) {
//                System.out.println("m1.getName() = " + m1.getName());
//            }

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
