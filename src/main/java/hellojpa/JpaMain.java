package hellojpa;

import domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        // 웹서버가 실행될때 1개만 생성되는것이다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            // JPQL
            // JPA Crieria

            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));

            List<Member> result = em.createQuery(
                    "select m from Member m where m.name like '%kim%'",
                    Member.class
            ).getResultList();

            // QueryDSL
            // 네이티브 SQL

            // JDBC API 직접 사용, MyBatis, Spring jdbcTemplate 함께 사용 가능

//            TestClass test = new TestClass.TestClassBuilder("i9", "500GB")
//                    .setGraphicCardEnabled(true)
//                    .setBluetoothEnabled(true)
//                    .build();
//
//            test.getCpu();
//            test.getHdd();
//            test.isGraphicCardEnabled();
//            test.isBluetoothEnabled();
//
//            Member member = new Member();
//
//            member.setName("root");
//            member.setAddress(new Address("111", "222", "333"));
//
//            member.getFavoriteFoods().add("치킨");
//            member.getFavoriteFoods().add("족발");
//            member.getFavoriteFoods().add("피자");
//
//            member.getAddressHistory().add(new AddressEntity("city", "street", "zipcode"));
//            member.getAddressHistory().add(new AddressEntity("city1", "street1", "zipcode1"));
//
//            // 라이프사이클이 같이 돌아간다.
//            // 값타입 컬렉션도 본인의 생명주기가 없다.
//            // 모든 컬렉션의 생명주기가 엔티티에 의존한다.
//            em.persist(member);
//
//            em.flush();
//            em.clear();
//
//            Member findMember = em.find(Member.class, member.getId());
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for(String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFoods = " + favoriteFood);
//            }
//            Address a = findMember.getAddress();
//            findMember.setAddress(new Address("newCity", a.getStreet(), a.getZipcode()));
//
//            //치킨 => 한식
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");
//
//            findMember.getAddressHistory().remove(new AddressEntity("city", "street", "zipcode"));
//            findMember.getAddressHistory().add(new AddressEntity("city2", "street2", "zipcode2"));

            //Address address = new Address();
            //Address address1 = new Address();

            // 자바는 참조값을 비교하기 때문에 == 결과가 false
            //System.out.println("address1 == address " + (address1 == address));
            // 기본 equals()는 == 비교이다.
            // equals()를 재정의해서 사용해야한다. 주로 모든필드를 사용
            //System.out.println("address1.equals(address) = " + address1.equals(address));
            // 동일성 비교 == 인스턴스의 참조값 비교
            // 동등성 비교 equals() 인스턴스의 값을 비교

//            Address address = new Address("111", "222", "333");
//
//            Member member1 = new Member();
//            member1.setName("member1");
//            member1.setAddress(address);
//            em.persist(member1);
//
//            Address address1 = new Address(address.getCity(), address.getStreet(), "zipzip");
//
//            Member member2 = new Member();
//            member2.setName("member2");
//            member2.setAddress(address1);
//            em.persist(member2);

            // 자바의 기본타입은 값을 복사함으로 복사한 변수를 변경해도 서로 영향을 받지 않음
            //int a = 10;
            //int b = a;
            //b = 4;

            // 자바의 객체타입은 값을 복사하는것이 아니라 참조를 공유한다.
            // = 연산자를 막을 수 있는 방법이 없다.
            //Address address2 = new Address("city", "street", "zipcode");
            //Address address3 = address2;

            // 따라서 불변 객체로 설정해야한다.
            // String과 Integer가 대표적
            // 생성장로만 설정하고 수정자를 막는것 setter X, setter를 private로 변경
            // 값을 변경하고 싶은 경우 아래와 같이 새로 만들어서 변경해야한다.
            // 아니면 다른 방법들도 있으니 찾아봐라.
            // 불변으로 설정함으로서 다른 데이터를 변경하는 큰 재앙을 막을수 있다.
            //Address newAddress = new Address(address2.getCity(), address2.getStreet(), "zipcode2");



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
