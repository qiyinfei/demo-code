package com.tmindtech.api.demoserver.example.model;

import java.text.DecimalFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;

    public String name;

    public String phone;

    public Integer age;

    public Double salary;

    public static void main(String[] args) {
        Integer decimal = 213400;
        System.out.println(new DecimalFormat("0.00").format(decimal / 100));
    }

}
