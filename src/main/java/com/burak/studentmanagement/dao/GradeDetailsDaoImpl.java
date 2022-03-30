package com.burak.studentmanagement.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.GradeDetails;

@Repository
public class GradeDetailsDaoImpl implements GradeDetailsDao {
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public void save(GradeDetails gradeDetails) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(gradeDetails);
	}

	@Override
	public GradeDetails findById(int id) {
		Session session = entityManager.unwrap(Session.class);
		Query<GradeDetails> query = session.createQuery("from grade_details where id=:gradeId", GradeDetails.class);
		query.setParameter("gradeId", id);
		
		try {
			return query.getSingleResult();
		} catch (Exception exc) {
			return null;
		}
	}

	@Override
	public void deleteById(int gradeDetailsId) {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete grade_details where id=:gradeDetailsId");
		query.setParameter("gradeDetailsId", gradeDetailsId);
		query.executeUpdate();
	}

}
