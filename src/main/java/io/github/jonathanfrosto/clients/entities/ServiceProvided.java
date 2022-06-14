package io.github.jonathanfrosto.clients.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ServiceProvided extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
