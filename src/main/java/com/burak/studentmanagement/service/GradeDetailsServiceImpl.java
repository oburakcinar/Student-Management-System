package com.burak.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.GradeDetailsDao;
import com.burak.studentmanagement.entity.GradeDetails;

@Service
public class GradeDetailsServiceImpl implements GradeDetailsService {

	@Autowired
	private GradeDetailsDao gradeDetailsDao;
	
	@Override
	@Transactional
	public void save(GradeDetails gradeDetails) {
		gradeDetailsDao.save(gradeDetails);
	}

	@Override
	@Transactional
	public GradeDetails findById(int id) {
		return gradeDetailsDao.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		gradeDetailsDao.deleteById(id);
	}

}
