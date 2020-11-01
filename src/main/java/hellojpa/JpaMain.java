package hellojpa;

import domain.Address;
import domain.Member;
import domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {

    public static void main(String[] args) {

        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Address address = new Address();
            address.setCity("111");
            address.setStreet("111");
            address.setZipcode("111");

            Member member1 = new Member();
            member1.setName("member1");
            member1.setAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setAddress(address);
            em.persist(member2);

            member1.getAddress().setCity("zzz-");

            member1.getName();

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
