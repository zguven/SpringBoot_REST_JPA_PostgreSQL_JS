package com.selimsql.springboot.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.selimsql.springboot.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

  private EntityManager entityManager;

  public UserDAOImpl() {
  }

  @PersistenceContext
  private void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  @Override
  public User findOne(Long id) {
    System.out.println("findOne.keyId:" + id);

    if (id==null)
      return null;

    User row = entityManager.find(User.class, id);
    return row;
  }


  @Override
  public List<User> findAll() {
    System.out.println("findAll");

    try {
      String sql = "Select t From User t Order by t.id";
      TypedQuery<User> query = entityManager.createQuery(sql, User.class);

      List<User> list = query.getResultList();

      return list;
    }
    catch (Throwable ext) {
      throw ext;
    }
  }

  
  @Override
  public User insert(User user) {
    System.out.println("insert row: " + user);
    entityManager.persist(user);
    return user;
  }


  @Override
  public User update(User user) {
    System.out.println("update row: " + user);
    User userUpdated = entityManager.merge(user);
    return userUpdated;
  }

  @Override
  public void delete(User user) { 
    System.out.println("delete row: " + user);
    entityManager.remove(user);
  }
}
