package myapp

import grails.util.Environment

class BootStrap {

    PersonService personService

    def init = { servletContext ->
        if (Environment.current == Environment.DEVELOPMENT) {
            (1..100).each {
                Person person = new Person(firstName: "Michel-$it", lastName: "heyd-$it")
                personService.save(person)
            }
        }
    }
    def destroy = {
    }
}
