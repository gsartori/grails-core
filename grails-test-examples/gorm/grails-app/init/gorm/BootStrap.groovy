package gorm

import gorm.*

class BootStrap {

    def init = { servletContext ->
        Book.withTransaction {
            def b = new Book(title:"The Stand")

            b.save(flush:true)


            def city = new City(name:"London")
            city.addToUsers(name:"Bob")
            city.addToUsers(name:"Fred")
            city.save(flush:true)

            def paris = new City(name:"Paris")
            paris.addToUsers(name:"Joe")
            paris.save(flush:true)
        }

    }
    def destroy = {
    }
}
