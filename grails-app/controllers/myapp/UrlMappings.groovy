package myapp

class UrlMappings {

    static mappings = {

        "/person/list"(controller: "person", action: "index")
        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
