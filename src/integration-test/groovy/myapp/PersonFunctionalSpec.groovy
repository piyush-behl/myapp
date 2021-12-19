package myapp

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Integration
class PersonFunctionalSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client
    PersonService personService

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    @Override
    void setMetaClass(MetaClass metaClass) {
        super.setMetaClass(metaClass)
    }

    void "To test rest end point person list without max value"() {

        given: "there are 2 person records in the database"
        (1..2).each {personService.save("fname$it", "lname$it")}

        when: "when user makes a GET - localhost:8080/person/list"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list"), PersonListResult)

        then: "list of 2 person data should be returned"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 2
        resp.results.sort().toString() == [new PersonData(firstName: 'fname1', lastName: 'lname1'), new PersonData(firstName: 'fname2', lastName: 'lname2')].sort().toString()
    }

    void "To test rest end point person list without max value should return 10 records"() {

        given: "there are 10 person records in the database"
        (1..10).each {personService.save("fname$it", "lname$it")}

        when: "when user makes a GET - localhost:8080/person/list"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list"), PersonListResult)

        then: "list persons should return 10 person records"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 10
        resp.results.sort().toString() == (1..10).collect {new PersonData(firstName:  "fname$it", lastName:  "lname$it")}.sort().toString()
    }

    void "To test rest end point person list without max value (default 10)"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "when user makes a GET - localhost:8080/person/list   "
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list"), PersonListResult)

        then: "list of 10 person data should be returned instead of 20"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 10
        resp.results.sort().toString() == (1..10).collect {new PersonData(firstName:  "fname$it", lastName:  "lname$it")}.sort().toString()
    }

    void "To test rest end point person list with max value 20"() {

        given: "there are 30 person records in the database"
        (1..30).each {personService.save("fname$it", "lname$it")}

        when: "user makes a GET - localhost:8080/person/list?max=20"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list?max=20"), PersonListResult)

        then: "list of 20 person data should be returned"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 20
        resp.results.sort().toString() == (1..20).collect {new PersonData(firstName:  "fname$it", lastName:  "lname$it")}.sort().toString()
    }

    void "To test rest end point person list with max value -1"() {

        given: "there are 20 person records in the database"
        (1..21).each {personService.save("fname$it", "lname$it")}

        when: "user makes a GET - localhost:8080/person/list?max=-1"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list?max=-1"), PersonListResult)

        then: "list of 20 person data should be returned"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 21
        resp.results.sort().toString() == (1..21).collect {new PersonData(firstName:  "fname$it", lastName:  "lname$it")}.sort().toString()
    }

    void "To test rest end point person list with invalid max value"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "user makes a GET - localhost:8080/person/list?max=abc"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/person/list?max=abc"), PersonListResult)

        then: "list of 10 person data should be returned"
        response.status == HttpStatus.OK
        PersonListResult resp = response.body()
        resp.status == 'success'
        resp.count == 10
        resp.results.sort().toString() == (1..10).collect {new PersonData(firstName:  "fname$it", lastName:  "lname$it")}.sort().toString()
    }

    void "To test rest end point person list with POST type"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "user makes a POST - localhost:8080/person/list?max=20"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.POST("/person/list",[:]), PersonListResult)

        then: "api should return 405 error message method not allowed"
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.METHOD_NOT_ALLOWED
    }


    void "To test rest end point person list with PUT type"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "user makes a PUT - localhost:8080/person/list?max=20"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.PUT("/person/list",[:]), PersonListResult)

        then: "api should return 405 error message method not allowed"
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.METHOD_NOT_ALLOWED
    }

    void "To test rest end point person list with DELETE type"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "user makes a DELETE - localhost:8080/person/list?max=20"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.DELETE("/person/list",[:]), PersonListResult)

        then: "api should return 405 error message method not allowed"
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.METHOD_NOT_ALLOWED
    }

    void "To test rest end point person list with invalid url"() {

        given: "there are 20 person records in the database"
        (1..20).each {personService.save("fname$it", "lname$it")}

        when: "user makes a GET - localhost:8080/people/list?max=20"
        HttpResponse<PersonListResult> response = client.toBlocking().exchange(HttpRequest.GET("/people/list"), PersonListResult)

        then: "api should return 404 error message"
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.NOT_FOUND
    }

}
