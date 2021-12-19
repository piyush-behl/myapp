package myapp

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PersonSpec extends Specification implements DomainUnitTest<Person> {

    def setup() {
    }

    def cleanup() {
    }

    void "test save person with null constraints"() {
       when:
       Person person = new Person()
       person.save()
       then:
       person.hasErrors()
       person.errors.getFieldError("firstName").code == "nullable"
       person.errors.getFieldError("lastName").code == "nullable"
    }

    void "test save person with unique constraints"() {
        given:
        new Person(firstName: "fname", lastName: "lname").save(flush: true, failOnError: true)
        when:
        Person person = new Person(firstName: "fname", lastName: "lname")
        person.save()
        then:
        person.hasErrors()
        person.errors.getFieldError("firstName").code == "unique"
    }

    void "test a valid save person"() {
        when:
        Person person = new Person(firstName: "fname", lastName: "lname")
        person.save()
        then:
        !person.hasErrors()
        Person.count() == old(Person.count()) + 1
    }
}
