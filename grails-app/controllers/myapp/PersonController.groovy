package myapp

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class PersonController {

    PersonService personService

    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", index: "GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond personService.list(params), model:[personCount: personService.count()]
    }

    def show(Long id) {
        respond personService.get(id)
    }

    @Transactional
    def save(Person person) {
        if (person == null) {
            render status: NOT_FOUND
            return
        }
        if (person.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond person.errors
            return
        }

        try {
            personService.save(person)
        } catch (ValidationException e) {
            respond person.errors
            return
        }

        respond person, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Person person) {
        if (person == null) {
            render status: NOT_FOUND
            return
        }
        if (person.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond person.errors
            return
        }

        try {
            personService.save(person)
        } catch (ValidationException e) {
            respond person.errors
            return
        }

        respond person, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null || personService.delete(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
}
