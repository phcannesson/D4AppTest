# Projet de test D4 Mobile App Android

Je te propose un petit challenge de code afin de voir ta façon de penser.

## Concept de l'app

L'application que tu vas faire doit permettre de rechercher par nom des projets Android/iOS sur Github (se limiter à une dizaine de résultats).
La doc API est ici : https://developer.github.com/v3/search/#search-repositories

Elle doit être composée d'une vue avec :
- un edit text pour taper le texte que l'on recherche,
- un recyclerview (ou autre si tu veux) pour afficher les résultats,
- un bouton pour switcher entre Android/iOS

Ce sont des grandes lignes, tu as carte blanche pour organiser ton écran comme tu le souhaites.


## Consignes

- La feature doit être développée en MVP (avec un contract)
- Les librairies à utiliser : RxJava, Retrofit et le tout en Kotlin
- Tu peux ignorer tout ce qui est injection de dépendance, et créer le manager à la volée.
- Tu peux ignorer les problématiques de rotation de l'écran
- Aucun stockage en local, on ne travaille qu'en mémoire
- Une attention particulière sera portée sur la gestion des messages d'erreur (notamment lorsque l'utilisateur n'a pas de réseau)
- Evitons de faire trop d'appels réseaux inutiles sur github.com si l'utilisateur est en train de taper


## A faire

- J'ai initializé un projet vide avec une activité nommée GithubSearchActivity. C'est celle-ci que tu vas modifier.
- Fais une branche pour ton dev et lorsque tu as terminé, fais une Pull Request.

Have fun !

Si tu as des questions, n'hésites pas à me les faire parvenir sur paulhubert.cannesson.partner@decathlon.com
