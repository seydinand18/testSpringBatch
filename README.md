# Projet Spring Batch - Importation des Étudiants

Ce projet utilise Spring Batch pour lire des données à partir d'un fichier CSV et les importer dans une base de données.
Les données concernent des étudiants, incluant leur prénom, nom et âge.

## Table des Matières

- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Fonctionnalités](#fonctionnalités)
- [Autheur](#autheur)

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi) ou [Gradle](https://gradle.org/install/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Base de données](https://www.postgresql.org/) (PostgreSQL recommandé)

## Installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/xxx/xxx.git
   cd votre-projet
   ```

2. Compilez et construisez le projet :
   ```bash
   mvn clean install
   ```

## Configuration

1. Assurez-vous que le fichier `students.csv` est présent dans le répertoire `src/main/resources/`.

2. Modifiez le fichier `application.yml` pour configurer votre connexion à la base de données, si nécessaire.

## Utilisation

Pour exécuter le job d'importation des étudiants, vous pouvez utiliser l'API REST suivante :

- **Endpoint** : `POST /api/v1/student/import-student`

### Exemple avec `curl` :

```bash
curl -X POST http://localhost:8080/api/v1/student/import-student
```

## Fonctionnalités

- Lecture des données à partir d'un fichier CSV.
- Transformation des données (par exemple, mise en majuscules des noms de famille).
- Persistance des données dans la base de données.

## Autheur

- [Mouhamed Ndiaye]() - Développeur Java
