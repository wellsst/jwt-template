package jwt.template

class UrlMappings {

    static mappings = {
        "/signup" (controller: "login", action: "signup")
        "/login" (controller: "login", action: "login")
        "/signupRequest" (controller: "login", action: "signupRequest")
        "/requestJWT/$requestId" (controller: "login", action: "requestJWT")
        "/loginWithJWT" (controller: "login", action: "loginWithJWT")

        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
