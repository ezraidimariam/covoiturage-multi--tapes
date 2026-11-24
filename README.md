# Covoiturage multi-étapes

API REST de covoiturage avec des trajets composés de plusieurs points d'arrêt. Les passagers peuvent rechercher un trajet et réserver un segment entre deux arrêts.

## Fonctionnalités

- Gestion des conducteurs, passagers et véhicules.
- Création, modification, consultation et suppression des trajets et points d'arrêt.
- Publication et annulation de trajets.
- Recherche paginée par ville de départ, ville d'arrivée, date et nombre de places, avec un prix maximal facultatif.
- Réservation entre deux points d'arrêt, avec vérification des places disponibles et calcul du montant du segment.
- Annulation de réservation.

## Technologies

| Technologie | Version ou rôle |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.2 |
| Spring Web MVC | API REST |
| Spring Data JPA / Hibernate | Persistance et ORM |
| PostgreSQL | 16, base de données |
| Maven Wrapper | Build et lancement |
| Docker Compose | Base de données locale |
| MapStruct | 1.6.3, mapping DTO/entités |
| Jakarta Bean Validation | Validation des entrées |
| JUnit, Mockito, AssertJ | Tests |

## Prérequis

- JDK 17.
- Docker avec la commande `docker compose` (pour la base locale).
- Git, si vous clonez le dépôt.

Le dépôt contient le Maven Wrapper : il n'est pas nécessaire d'installer Maven séparément.

## Démarrage local

Clonez le dépôt et ouvrez son dossier :

```bash
git clone https://github.com/ezraidimariam/covoiturage-multi--tapes.git
cd covoiturage-multi--tapes
```

Le projet inclut PostgreSQL 16 dans `docker-compose.yml`. Avec Docker en marche, démarrez la base :

```bash
docker compose up -d
```

L'intégration Spring Boot Docker Compose est activée dans `application.properties`. Vous pouvez ensuite lancer l'API avec le wrapper Maven.

Windows PowerShell :

```powershell
.\mvnw.cmd spring-boot:run
```

Linux ou macOS :

```bash
./mvnw spring-boot:run
```

L'API écoute sur [http://localhost:8082](http://localhost:8082).

Pour arrêter PostgreSQL sans supprimer les données persistées :

```bash
docker compose down
```

## Configuration locale

Les paramètres sont définis dans `src/main/resources/application.properties` et `docker-compose.yml`.

| Paramètre | Valeur locale |
|---|---|
| Base PostgreSQL | `covoiturage` |
| Utilisateur | `postgres` |
| Mot de passe | `postgres` |
| Port PostgreSQL sur l'hôte | `15433` |
| URL JDBC | `jdbc:postgresql://127.0.0.1:15433/covoiturage` |
| Port HTTP | `8082` |

Ces identifiants sont ceux de la configuration de développement fournie. Hibernate est configuré avec `ddl-auto=update` et l'affichage SQL est activé.

## API

Les contrôleurs sont préfixés par `/api`. Les ressources principales sont :

| Ressource | Route |
|---|---|
| Conducteurs | `/api/conducteurs` |
| Passagers | `/api/passagers` |
| Véhicules | `/api/vehicules` |
| Trajets | `/api/trajets` |
| Points d'arrêt | `/api/points-arret` |
| Réservations | `/api/reservations` |

Les routes de collection prennent en charge la création et la liste; les routes `/{id}` fournissent les opérations par identifiant définies par chaque contrôleur. Un trajet est créé en brouillon (statut `null`), ses points sont ajoutés, puis il est publié après validation de tout l'itinéraire. Les points et le trajet ne sont modifiables qu'avant publication.

Routes complémentaires :

- `GET /api/conducteurs/rechercher?nom=...` ou `?email=...` : recherche d'un conducteur.
- `GET /api/conducteurs/{id}/reservations` : réservations des trajets du conducteur.
- `GET /api/passagers/rechercher?email=...` : recherche d'un passager.
- `GET /api/passagers/{id}/reservations` : historique des réservations du passager.
- `GET /api/vehicules/conducteur/{id}` : véhicules d'un conducteur.
- `POST /api/trajets/{id}/terminer` : termine un trajet après son départ.

`GET /api/trajets/{id}` renvoie le parcours ordonné, les places restantes et le conducteur. Les erreurs de l'API utilisent le format `ProblemDetail` avec l'heure, le code, le type, le message et le chemin.

### Recherche de trajets

`GET /api/trajets/rechercher` accepte les paramètres suivants :

| Paramètre | Type | Requis |
|---|---|---|
| `villeDepart` | Texte | Oui |
| `villeArrivee` | Texte | Oui |
| `dateDepart` | Date ISO (`YYYY-MM-DD`) | Oui |
| `nombrePlaces` | Entier, au moins 1 | Oui |
| `prixMax` | Nombre décimal | Non |
| `page`, `size`, `sort` | Paramètres de pagination Spring Data | Non |

Exemple :

```text
http://localhost:8082/api/trajets/rechercher?villeDepart=Safi&villeArrivee=Casablanca&dateDepart=2026-12-01&nombrePlaces=2&page=0&size=10
```

## Tests

Windows PowerShell :

```powershell
.\mvnw.cmd test
```

Linux ou macOS :

```bash
./mvnw test
```

Une collection Postman pour le flux principal est disponible dans `postman/covoiturage.postman_collection.json`.

## Limite d'accès

L'application n'a pas de mécanisme de connexion ou d'identité authentifiée. Elle vérifie qu'un véhicule appartient au conducteur indiqué lors de la création d'un trajet, mais ne peut pas garantir qu'une requête de consultation, d'annulation ou de modification vient réellement du conducteur ou du passager concerné. Une authentification et une autorisation par rôle sont nécessaires avant une mise en production.

## Structure du code

```text
src/main/java/ma/youcode/covoiturage/
├── controller/   # Contrôleurs REST
├── dto/          # Objets d'entrée et de sortie de l'API
├── entity/       # Entités JPA et statuts
├── mapper/       # Conversion DTO/entités
├── repository/   # Accès aux données
└── service/      # Logique applicative
```
