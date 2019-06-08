environments {
    production {
        // Follow will be for deploy to Heroku
        dataSource {
            dbCreate = "update"
            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQL94Dialect
            uri = new URI(System.env.DATABASE_URL?:"postgres://localhost:5432/test")
            url = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require"
            username = uri.userInfo?.split(":")[0]
            password = uri.userInfo?.split(":")[1]
        }
        grails {
            serverURL = "changeme.herokuapp.com"
            host = "smtp.gmail.com"
            port = 465
            username = "youracount@gmail.com"
            password = "yourpassword"
            props = ["mail.smtp.auth":"true",
                     "mail.smtp.socketFactory.port":"465",
                     "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                     "mail.smtp.socketFactory.fallback":"false"]
        }
    }
}


