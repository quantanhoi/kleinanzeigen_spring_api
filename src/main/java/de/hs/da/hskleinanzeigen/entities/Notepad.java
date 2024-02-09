package de.hs.da.hskleinanzeigen.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "NOTEPAD")
public class Notepad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @JoinColumn(name = "USER_ID")
    @ManyToOne
    private User user;
    @JoinColumn(name = "AD_ID")
    @ManyToOne
    private Advertisement advertisement;
    private String note;
    @CreationTimestamp
    private Timestamp created;

}
