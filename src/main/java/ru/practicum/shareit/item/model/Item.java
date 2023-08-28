package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"name", "description", "available", "owner"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @OneToOne
    @JoinColumn(name = "ownerId")
    private User owner;
}