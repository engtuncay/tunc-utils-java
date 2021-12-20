package ozpasyazilim.utils.datatypes;

import java.util.Date;

/**
 * Test Amaçlı Kullanılabilecek Entity
 *
 */
public class PersonEntityTest {

	String name;
	String surname;
	Integer age;
	Date birthDate;
	Boolean isMarried;
	PersonEntityTest person;

  public PersonEntityTest() {
  }

	  public PersonEntityTest(String name, String surname) {
			this.name = name;
			this.surname = surname;
	  }

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

	public String getSurname() {return surname;}

	public void setSurname(String surname) {this.surname = surname;}

	public Integer getAge() {return age;}

	public void setAge(Integer age) {this.age = age;}

	public Date getBirthDate() {return birthDate;}

	public void setBirthDate(Date birthDate) {this.birthDate = birthDate;}

	public Boolean getMarried() {return isMarried;}

	public void setMarried(Boolean married) {isMarried = married;}

	public PersonEntityTest getPerson() {return person;}

	public void setPerson(PersonEntityTest person) {this.person = person;}

}
