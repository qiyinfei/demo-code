package com.tmindtech.api.demoserver.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class ModelBeanUtil {

    public static void checkNotEmpty(Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.asList(fields).forEach(field -> {
            try {
                Object obj = field.get(object);
                if (Objects.isNull(obj)) {
                    String str = "%s的值为空";
                    throw new RuntimeException(String.format(str, field.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public static void map(Object origin, Object current) {
        Field[] oFields = origin.getClass().getDeclaredFields();
        Field[] cFields = current.getClass().getDeclaredFields();
        for (Field oField : oFields) {
//            oField.setAccessible(true);
            for (Field cField : cFields) {
//                cField.setAccessible(true);
                if (oField.getName().equals(cField.getName())) {
                    try {
                        oField.set(origin, cField.get(current));
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
//        checkNotEmpty(new Person(null, "java", 23, 5525));
        Person person = new Person("小齐", "java", 22, 5555);
        System.out.println("map之前：" + person.toString());
        newPerson newPerson = new newPerson("小兵", "小花", 33, 2);
        map(person, newPerson);
        System.out.println("map之后：" + person.toString());
    }

}

class Person {
    public String name;
    public String job;
    public int age;
    public int salary;

    public Person(String name, String job, int age, int salary) {
        this.name = name;
        this.job = job;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}

class newPerson {
    public String name;
    public String wife;
    public int age;
    public int kids;

    public newPerson(String name, String wife, int age, int kids) {
        this.name = name;
        this.wife = wife;
        this.age = age;
        this.kids = kids;
    }
}