package com.burak.studentmanagement.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.Teacher;

@Repository
public class TeacherDaoImpl implements TeacherDao {
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Teacher findByTeacherName(String theTeacherName) {
		Session session = entityManager.unwrap(Session.class);
		Query<Teacher> query = session.createQuery("from Teacher where userName=:user", Teacher.class);
		query.setParameter("user", theTeacherName);
		
		try {
			return query.getSingleResult();
		} catch (Exception exc) {
			return null;
		}
	}
	
	@Override
	public Teacher findByTeacherId(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<Teacher> query = session.createQuery("from Teacher where id=:teacherId", Teacher.class);
		query.setParameter("teacherId", id);
		
		try {
			return query.getSingleResult();
		} catch (Exception exc) {
			return null;
		}
	}

	@Override
	public void save(Teacher teacher) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(teacher);
	}

	@Override
	public List<Teacher> findAllTeachers() {
		Session session = entityManager.unwrap(Session.class);
		List<Teacher> teachers = session.createQuery("from Teacher", Teacher.class).getResultList();
		return teachers;
	}

	@Override
	public void deleteTeacherById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete Teacher where id=:teacherId");
		query.setParameter("teacherId", id);
		query.executeUpdate();
	}

	

}
