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
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String email;

    private String password;

    private String first_name;

    private String last_name;

    private String phone;

    private String location;

    @CreationTimestamp
    private Timestamp created;

    @OneToMany(mappedBy = "user")
    private Set<Notepad> notepads = new HashSet<>();

}
