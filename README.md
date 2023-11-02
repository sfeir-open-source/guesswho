# Qui est-ce

## How to add a theme

Currently, it is not possible to add a theme using the interface, it must be done in the code and the project must be
re-delivered. Follow these steps to add a theme:

    1) Gather the images for your theme. You need one image for the theme overview, and then as many images as you want
        for the cards.
    2) In src/main/webapp/content/images/themes, create a folder for your theme (the name won't be visible for users).
    3) Move all your images in this folder (including the overview image). Avoid spaces in the folder/file-names.
    4) Open src/main/resources/config/liquibase/data/picture.csv, and add one line for each image  (including the
        overview image) in the same format as existing lines. Use incrementing IDs (start from last existing one + 1).
        The path is relative to src/main/webapp/content/images/themes/ (so <themeFolderName>/<imageName>).
    5) Open src/main/resources/config/liquibase/data/theme.csv and add one line for your theme. For main_picture_id, use
        the ID of the overview picture from picture.csv.
    6) Open src/main/resources/config/liquibase/data/theme_card.csv, and add one line for each card. Use incrementing
        IDs (start from last existing one + 1). theme_id is the ID from theme.csv, and picture_id is the ID from
        picture.csv.
    7) Delete target/h2db (the development database - it will be re-generated when starting the app locally).
    8) That's it! You can now build and test the project.



## Roadmap - features & technical improvements

Features:

    - login using Google (and remove existing user-password authentication and manual account creation, password reset, etc)
    - add other themes
    - make the interface mobile-friendly (responsive) and improve it so that all cards are visible without scrolling on desktop
    - let the user undo a card removal (if he discarded it by mistake and notice afterwards)
    - admin interface to add themes (and user interface to propose themes, that admin can validate ?)
    - play against AI (an AI will just be another type of "player" entity, that will react to application-triggered events).
    - add descriptions to cards (to be used in <img> alt attribute)
    - add copyright field for theme images (for instance https://devicon.dev/ for "programming-languages" theme) and display the copyright.
    - let a user delete old rooms
    - button to end a game (no one wins, and a new game can be started - useful if you do not end a game and come back some days later)
    - let anonymous users join rooms (using existing user_anonymous entity)
    - when user A win because user B guessed the wrong card, the card user B had to guess is displayed to user B, but we
        should also display to user A the card he was trying to guess (so that he can see if he discarded that card or not).

Technical improvements / fixes:

    - backend:
        - some tests are flaky (sometimes failing, sometimes not)
            - at least GameResourceIT.getGameDetailsWhenWon ("$.gameCards[1].player2_chosen" expected true got false),
                GameResourceIT.playAndLoose ("$.gameCards[0].player1_chosen" expected true got false)
                and GameResourceIT.playAndWin ("$.gameCards[0].player1_chosen" expected true got false)
            - when starting the tests with IntelliJ, they pass, and when starting the tests using the CLI, they fail.
            - this is probably due to the fact that tests are not independent (database not reset between tests ?)
            - to be investigated!
        - replace some of the AccessDeniedException (403) by a BadRequestException (400)
        - make sure no stack traces are sent to frontend (e.g. in case of error 500)
        - refactoring: avoid fields player1,player2, better use a table (easier to factorise the code)
        - hibernate - check that queries are efficient (no N+1 issue ?)
        - add endpoint GET /api/room/{id}
        - GET /api/games: return less data (do not return all cards), details can be retrieved through /api/games/{id}
        - /api/themes/{id} should return an object, not the array of theme cards
        - create different DTOs for endpoints (currently full objects are returned with null fields - example:
              /api/games: $.room.player1 is null => instead, just do not include this field in the response) 
        - use web sockets for game status updates (new message, other player played) => less data transmitted faster
        - cleanup useless jhipster dependencies ? Or get rid of JHipster now that the project is generated ?
    - frontend:
        - add tests (services, components)
        - improve error management (if play() request fails, display/explain error)
            - eg: if user try to create a game at the same time as another user, one of the request will fail. Explain the error.
        - add a store
        - update code structure: get rid of JHipster (for frontend)
        - move ineline css to scss files



## Deployment guide

### Build

Before building the project, make sure docker is started on your machine, as it is needed for integration tests.

To build the project for production: ./mvnw -Pprod clean verify
Same but skipping tests (some tests are flaky for now... ): ./mvnw -Pprod clean verify -DskipTests

It will generate a jar file in the target/ folder. If you need a war file (to deploy on an app server like Tomcat), you
can update the line <packaging>jar</packaging> in pom.xml to <packaging>war</packaging> and build the project again.


### Test the build locally

Start a local database in a terminal:
    - docker compose --file src/main/docker/postgresql.yml up

Start the app in another terminal:
    - export SPRING_DATASOURCE_PASSWORD=test-for-local-dev-only
    - java -jar target/quiestce.jar

There will be no users available. If you want to temporarily add a user, you can:
    - add users in src/main/resources/config/liquibase/data/user.csv
    - add their role in src/main/resources/config/liquibase/data/user_authority.csv
    - => check examples in fake-data/user.csv and fake-data/user_authority.csv: add similar lines - just use other IDs.

But do not forget to remove these users before deploying in production (or change their password)!

When you are done:
    - ctrl+C in both terminals
    - remove containers:        docker compose --file src/main/docker/postgresql.yml down
    - delete local database:    docker volume rm quiestce_quiestce-postgres-db


### Deployment

TO DO - describe the deployment of the jar/war on GCP

Notes:
    - before deploying the app in production, make sure to review relevant sections in this page: https://www.jhipster.tech/security/
    - the app can be deployed on an application server (e.g. Tomcat): build the app as a war file, and add the war file
        in the deploy folder of the app server.
    - you can also deploy the app as a standalone jar: java -jar quiestce.jar
    - the app will try to connect to a Postgres database named "quiestce" on localhost:5432, with username quiestce
    - you need to set the password, for instance using an environment variable: SPRING_DATASOURCE_PASSWORD=xxx
    - you can also erase the database connexion string and the username:
        SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5433/dbname
        SPRING_DATASOURCE_USERNAME=quiestce
    - It is advised to dockerize the application (one openjdk container to start the app as standalone jar, and one 
        postgresql container for the database).

## Dev guide

### Commands

Before building the project, make sure docker is started on your machine, as it is needed for integration tests.

- Start backend only with live-reload: ./mvnw -P-webapp
- Start frontend only with live-reload: npm start
- Start backend and frontend for local testing: ./mvnw
- Login: http://localhost:8080
- Access swagger: http://localhost:8080/swagger-ui/index.html

There are 3 users available locally: admin/admin, admin2/admin, user/user

Notes:
    
    - when starting back and front with live reload (see commands above), you cannot easily test the game by opening two
        browsers and logging-in with one user in browser A and another user in browser B. The state of both pages will
        be synced (so if you change of page on one browser, the page will also change on the other browser). This is due
        to how the live-reload and local development is configured. So to test a game during development, you should 
        switch account each time you want to test an action as the other user. Another option is to use './mvnw' to 
        start the back and the front at once.
    - when updating initial database schema or (fake) data, you should delete target/h2db folder in case you have issues


### Structure

- The project follows the standard maven structure: backend code in src/main/java, tests in src/test, frontend in src/main/webapp

#### Backend

- Database:
    - Database tables definitions can be found in src/main/resources/config/liquibase/changelog
    - Default data: src/main/resources/config/liquibase/data (files referenced in the xmls in changelog)
    - Fake data (for local dev and tests): in src/main/resources/config/liquibase/fake-data (files referenced in the xmls in changelog)
    - Entities: in src/main/java/be/vandenn3/quiestce/domain
      - User: represents a real User (that can currently login with credentials, later with Google)
      - UserAnonymous: represents a non-logged-in user (to enable playing without an account - not implemented yet)
      - Player: represents a player (either a User, UserAnonymous, or later, an AI).
        - When a user is logged in, a player is created for him if it does not exist yet (see PlayerService.getPlayerForLoggedInUser)
      - Room: it is a place where two players can play games.
      - Game: represents one game.
      - Theme: a theme lists the existing cards (for instance: 25 programming languages)
      - ThemeCard: the list of cards for each theme.
      - GameCard: when a game is created (and its theme selected), one GameCard object is created for each ThemeCard.
            It contains the state of the card for each player (the card he chose, and the cards he discarded)
      - Message: represents a message in the chat of a game
      - Picture: a picture object. The file itself is currently included in frontend static files.
      - Authority: there are currently two roles: ROLE_USER and ROLE_ADMIN. Users should only have ROLE_USER.

#### Frontend

- Most of the code is in src/main/webapp/app/quiestce. Other files were mainly created by JHipster.

















# JHipster default README

This application was generated using JHipster 8.0.0-beta.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3](https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3).

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import 'leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the quiestce application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off forced authentication redirect for UI in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

Additionally, Instead of passing `sonar.password` and `sonar.login` as CLI arguments, these parameters can be configured from [sonar-project.properties](sonar-project.properties) as shown below:

```
sonar.login=admin
sonar.password=admin
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.0.0-beta.3 archive]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.0.0-beta.3/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/
