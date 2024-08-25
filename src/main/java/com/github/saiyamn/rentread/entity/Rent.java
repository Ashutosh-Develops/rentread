package com.github.saiyamn.rentread.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name="book_id")
    private Book book;

    private RentStatus rentStatus;

    public Rent(User user, Book book, RentStatus rentStatus) {
        this.user = user;
        this.book = book;
        this.rentStatus = rentStatus;
    }
}
