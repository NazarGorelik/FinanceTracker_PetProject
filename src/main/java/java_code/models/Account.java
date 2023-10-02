package java_code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Min(value = 0)
    @Column(name = "balance")
    private Double balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    private Person owner;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
