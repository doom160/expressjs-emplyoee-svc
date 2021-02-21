package com.renfa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id
  @Column(name = "id",  length = 5)
  private String id;

  
  @Column(name = "login", length = 15, unique=true)
  private String login;

  @Column(name = "name", length = 30)
  private String name;

  @Column(name = "salary")
  private float salary;

  public User() {

  }

  public User(String id, String login, String name, float salary) {
    this.id = id;
    this.login = login;
    this.name = name;
    this.salary = salary;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getSalary() {
    return salary;
  }

  public void setSalary(float isSalary) {
    this.salary = isSalary;
  }

  @Override
  public String toString() {
    return String.format("{id:%s, login:%s, name:%s, salary:%s}", id, login, name, salary);
  }

}
