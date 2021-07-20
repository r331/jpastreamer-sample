package dev.ivanov.jpastream.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Author {
    @Id
    private int id;
    private String name;
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private Set<Book> books;
}