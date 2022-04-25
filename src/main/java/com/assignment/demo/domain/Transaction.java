package com.assignment.demo.domain;

import com.assignment.demo.config.serializer.JsonDateTimeDeserializer;
import com.assignment.demo.config.serializer.JsonDateTimeSerializer;
import com.assignment.demo.domain.enums.PostgreSQLEnumType;
import com.assignment.demo.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
@Setter
@Getter
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Transaction {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Column(updatable = false)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private TransactionType type;

    @Column
    private BigDecimal amount;
}
