package de.hs.da.hskleinanzeigen.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "AD")
public class Advertisement {

    @Id
    @EqualsAndHashCode.Include
    private int id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Category category;

    private String title;
    private String description;
    private int price;
    private String location;

    @ManyToOne
    private User user;

    @CreationTimestamp
    private Timestamp created;
    @OneToMany(mappedBy = "advertisement")
    private Set<Notepad> notepads = new HashSet<>();

}
