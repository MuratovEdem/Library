package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
@EqualsAndHashCode(of = {"id"})
public class Book {
    @Id
    private Long id;
    private String name;
    private String author;
    private String date;
    private String genre;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "clients_books",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Client> clients = new HashSet<>();
}
