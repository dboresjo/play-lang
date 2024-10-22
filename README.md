# play-lang

Provides localisation support for play applications using a string interpolator 

Enables use of natural language in code directly:

```
given Lang = FRENCH
println(en"The Play Framework's localisation support was in need of some improvement.")
```
Outputs:

`"La prise en charge de la localisation de Play Framework avait besoin d'être améliorée."`

```
given Lang = SPANISH
println(en"The Play Framework's localisation support was in need of some improvement.")
```
Outputs:

`"El soporte de localización de Play Framework necesitaba algunas mejoras."`
