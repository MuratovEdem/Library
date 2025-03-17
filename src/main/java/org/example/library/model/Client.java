package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "clients")
@EqualsAndHashCode(of = {"id"})
public class Client {
    @Id
    private Long id;
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "clients_books",
        joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

}
