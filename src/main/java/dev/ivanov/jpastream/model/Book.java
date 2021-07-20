package dev.ivanov.jpastream.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Book {
    @Id
    private int id;
    private String title;
    private double price;
    private int year;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Author author;
}
