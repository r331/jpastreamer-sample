package dev.ivanov.jpastream;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import dev.ivanov.jpastream.model.Book;
import dev.ivanov.jpastream.model.Book$;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript;

class StreamerTest extends JpastreamApplicationTests {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    JPAStreamer jpaStreamer;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        try (var conn = dataSource.getConnection()) {
            executeSqlScript(conn, new ClassPathResource("data.sql"));
        }
    }

    @Test
    void allBooksTest() {
        var books = jpaStreamer.stream(Book.class).toList();
        assertEquals(8, books.size());
    }

    @Test
    void filterBooksTest() {
        var books = jpaStreamer.stream(Book.class)
                .filter(Book$.year.greaterOrEqual(2020))
                .toList();
        assertEquals(2, books.size());
    }

    @Test
    void twoFilterBooksTest() {
        var books = jpaStreamer.stream(Book.class)
                .filter(Book$.year.greaterOrEqual(2020))
                .filter(Book$.price.in(1000.0, 1700.0))
                .toList();
        assertEquals(1, books.size());
    }

    @Test
    void sortBooksTest() {
        var books = jpaStreamer.stream(Book.class)
                .sorted(Book$.price)
                .toList();
        assertEquals(100.0, books.get(0).getPrice());
    }


    @Test
    void complexSortBooksTest() {
        var books = jpaStreamer.stream(Book.class)
                .sorted(Book$.price.reversed().thenComparing(Book$.title.comparator()))
                .toList();
        assertEquals("Алгоритмы для начинающих", books.get(0).getTitle());
    }

    @Test
    void paginationBooksTest() {
        var books = jpaStreamer.stream(Book.class)
                .sorted(Book$.price)
                .skip(3)
                .limit(3)
                .toList();
        assertEquals(500.0, books.get(0).getPrice());
        assertEquals(1500.0, books.get(2).getPrice());
    }

    @Test
    void joinBooksTest() {
        var configuration = StreamConfiguration.of(Book.class)
                .joining(Book$.author);
        var authors = jpaStreamer.stream(configuration)
                .map(Book::getAuthor)
                .toList();
        assertEquals(8, authors.size());
    }
}
