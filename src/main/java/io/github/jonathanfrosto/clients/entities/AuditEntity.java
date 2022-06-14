package io.github.jonathanfrosto.clients.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@Getter
//@Setter
@MappedSuperclass
public abstract class AuditEntity {

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    private LocalDateTime updateTimestamp;

    private String creationUser;
    private String updateUser;
}
