package com.github.windbender.dao;

import java.util.List;

import com.github.windbender.core.Person;

public interface PersonDAO {
	
	public boolean checkPassword(String username, String password);
	public Person getPersonFromUsername(String username);
	Person storePerson(Person p);
	List<Person> getAllPersons();
	void updatePerson(Person person);
	void delete(String username);

}
