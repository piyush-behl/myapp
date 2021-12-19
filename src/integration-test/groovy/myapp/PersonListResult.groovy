package myapp

import groovy.transform.CompileStatic

@CompileStatic
class PersonListResult {

    String status
    List<PersonData> results
    Integer count
}
