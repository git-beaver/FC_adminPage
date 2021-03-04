package com.example.study.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //order_detail
@ToString(exclude = {"user", "item"}) //서로 상호참조하는 연관관계가 있는 변수는 exclude 해주어야함
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderAt;

    @ManyToOne // N:1
    private User user; //user로 설정하면 알아서 userId를 찾아감

    @ManyToOne
    private Item item;

}
