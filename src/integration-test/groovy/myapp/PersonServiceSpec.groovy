package myapp

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class PersonServiceSpec extends Specification {

    PersonService personService
    @Autowired Datastore datastore

    private Long setupData() {
        (1..4).each {
            new Person(firstName: "fname$it", lastName: "lname$it").save(flush:true, failOnError: true)
        }
        new Person(firstName: "fname", lastName: "lname").save(flush:true, failOnError: true).id
    }

    void "test get"() {
        setupData()

        expect:
        personService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Person> personList = personService.list(max: 2, offset: 2)

        then:
        personList.size() == 2
        personList[0].firstName == "fname3"
        personList[0].lastName == "lname3"
        personList[1].firstName == "fname4"
        personList[1].lastName == "lname4"
    }

    void "test count"() {
        setupData()

        expect:
        personService.count() == 5
    }

    void "test delete"() {
        Long personId = setupData()

        expect:
        personService.count() == 5

        when:
        personService.delete(personId)
        datastore.currentSession.flush()

        then:
        personService.count() == 4
    }

    void "test save"() {
        when:
        Person person = new Person(firstName: "fname5", lastName: "lname5")
        personService.save(person)

        then:
        person.id != null
    }
}
