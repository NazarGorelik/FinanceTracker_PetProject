package java_code.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty
    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "id"),
            @JoinColumn(name = "account_name", referencedColumnName = "name")}
    )
    private Account account;
}
