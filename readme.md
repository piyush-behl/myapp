> Response time: How will you ensure that calls to the endpoint are performant and do not take excessive time in a large production deployment?

This application ensure the response time by using pagination technique. We limit the maximum number of person in the list response to 100. Otherwise, it may cause the system to crash when there are millions of records in the database.

> Security: How will you ensure that only authorized users can access the data from this endpoint?

We can secure this application using Spring Security Core and Spring Security REST plugin, and secure list person endpoint for authorized users only.

> Quality: How will you ensure that your endpoint is thoroughly tested? How will you ensure that future engineers do not easily break what you have already built?

The end-to end functional tests ensure that the API is working as expected. If someone accidentally breaks the API then we should be able to see failing tests and revert the change immediately.

> Reusability: How will you ensure that this endpoint can be used outside of the single page application it is originally built for? How will you ensure that your underlying services can be used outside of the GET /person/list endpoint?

We are reading the data from database using GORM Data Service which is responsible for reading and writing person records. We can using this service in the controller to fetch person record information. We should be able to inject in any other controller or service within the application.