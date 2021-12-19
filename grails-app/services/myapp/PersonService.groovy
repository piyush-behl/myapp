package myapp

import grails.gorm.services.Query
import grails.gorm.services.Service

@Service(Person)
interface PersonService {

    Person get(Serializable id)

    List<Person> list(Map args)

    Long count()

    Person delete(Serializable id)

    Person save(Person person)

    Person save(String firstName, String lastName)

    @Query("delete from $Person")
    void deleteAll()
}
