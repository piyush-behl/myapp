package myapp

class Person {

    String firstName;
    String lastName;

    static constraints = {
        firstName unique: "lastName"
    }
}
