package myapp

import spock.lang.*
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import grails.validation.ValidationException
import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.gorm.DomainUnitTest
import grails.plugin.json.view.JsonViewGrailsPlugin

class PersonControllerSpec extends Specification implements ControllerUnitTest<PersonController>, DomainUnitTest<Person> {

    void setupSpec() {
        defineBeans(new JsonViewGrailsPlugin(applicationContext: applicationContext))
    }

    void "Test the index action returns the correct response"() {
        given:
        PersonService personService = Mock()
        controller.personService = personService

        when:"The index action is executed"
            controller.index(10)

        then:"The response is correct"
            1 * personService.list(_) >> []
            1 * personService.count() >> 0
    }

}
