package com.github.windbender.dao;

import java.util.List;

import com.github.windbender.core.Person;

public class DummyPersonDAO implements PersonDAO {

	@Override
	public boolean checkPassword(String username, String password) {
		if("secret".equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public Person getPersonFromUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person storePerson(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getAllPersons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePerson(Person person) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String username) {
		// TODO Auto-generated method stub

	}

}
