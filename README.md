# TutorTalk

TutorTalk is a platform for language lessons. Learners and tutors create an account, log in and chat with each other in real time. An administrator manages tutors, learners and languages from a dashboard.

I built TutorTalk on my own during my third semester of the Bachelor ICT at Fontys (March to June 2024, six sprints). This repository holds the whole project:

```
backend/    Spring Boot REST and WebSocket API
frontend/   React single-page app (Vite)
```

## Features

- **Accounts and roles:** learners and tutors sign up; learners, tutors and administrators log in.
- **Authentication:** Spring Security with JWT access tokens (valid for 30 minutes) and refresh tokens stored in the database. The front end keeps the tokens, refreshes the access token and guards its routes by role.
- **Management:** the admin dashboard creates, reads, updates and deletes tutors, learners and languages, and shows a learner count.
- **Real-time chat:** WebSockets with STOMP over SockJS. The back end offers a public chat room and private messages between users; the chat page in the front end is built around the private messages. Messages are saved, so the history can be loaded again.
- **API documentation:** OpenAPI / Swagger UI.

### Back-end endpoints

| Area      | Endpoints |
|-----------|-----------|
| Auth      | `POST /signup/learner`, `POST /signup/tutor`, `POST /tokens` (login), `POST /refresh_tokens/refresh`, `POST /refresh_tokens/validate` |
| Users     | `GET /users/username/{username}`, `GET /user-roles/{id}` |
| Tutors    | `GET /tutors`, `GET /tutors/{id}`, `POST /tutors`, `PUT /tutors/{id}`, `DELETE /tutors/{id}` |
| Learners  | `GET /learners`, `GET /learners/{id}`, `GET /learners/count`, `POST /learners`, `PUT /learners/{id}`, `DELETE /learners/{id}` |
| Languages | `GET /languages`, `GET /languages/{id}`, `POST /languages`, `PUT /languages/{id}`, `DELETE /languages/{id}` |
| Chat      | WebSocket endpoint `/ws`; send to `/app/message` (broadcast on `/chatroom/public`) or `/app/private-message` (delivered on `/user/{username}/private`); history via `POST /messages/save` and `GET /messages/all` |

While the back end runs, the full API documentation is at <http://localhost:8080/swagger-ui/index.html>.

## How it is built

### Back end

The code is split into layers, so the business rules do not depend on the web or database code and can be tested on their own:

```
backend/src/main/java/nl/fontys/s3/
├── controller/      REST and WebSocket controllers
├── business/        use case interfaces, validators and exceptions
│   └── impl/        use case implementations and converters
├── domain/          request and response objects
├── persistence/     Spring Data JPA repositories and entities
└── configuration/   security and JWT, WebSocket, CORS, Swagger, error handling, logging, dummy data
backend/src/main/resources/db/migration/   Flyway migrations V1–V8
```

**Stack:** Java 17 · Spring Boot 3.1 · Spring Security · JJWT · Spring WebSocket (STOMP) · Spring Data JPA · MySQL 8 · Flyway · springdoc-openapi · Lombok · JUnit 5 · Mockito · H2 · JaCoCo · SonarQube · Docker · GitLab CI · Gradle

### Front end

```
frontend/src/
├── api/          Axios calls per resource; TokenManager stores, decodes and refreshes the JWT
├── components/   nav bar, forms, lists, the chat room and the route guards (AuthLayout, AdminLayout)
├── pages/        home, about, login, sign-up, find a tutor and the admin dashboard
└── App.jsx       routes
frontend/cypress/ end-to-end tests
```

**Stack:** React 18 · Vite 5 · React Router 6 · MUI 5 · Axios · jwt-decode · SockJS + STOMP · Cypress · ESLint

## Run it locally

**Requirements:** JDK 17, a MySQL 8 server and Node.js 18 or newer.

### 1. Database

Create an empty database. The default configuration expects MySQL on port `3307`:

```sql
CREATE DATABASE tutortalk_db;
CREATE USER 'tutortalk'@'%' IDENTIFIED BY 'choose-a-password';
GRANT ALL PRIVILEGES ON tutortalk_db.* TO 'tutortalk'@'%';
```

### 2. Back end

The back end reads its credentials from environment variables:

| Variable         | Used for | Default |
|------------------|----------|---------|
| `DB_URL`         | JDBC URL of the database | `jdbc:mysql://localhost:3307/tutortalk_db` |
| `DB_USERNAME`    | database user | `tutortalk` |
| `DB_PASSWORD`    | database password | empty |
| `JWT_SECRET`     | Base64 key that signs the access tokens (at least 32 bytes) | none, required |
| `ADMIN_PASSWORD` | password of the admin account `admin@fontys.nl`, created on the first start | none, required |
| `SONAR_TOKEN`    | only for `./gradlew sonar` | empty |

```bash
cd backend
export DB_USERNAME=tutortalk
export DB_PASSWORD=choose-a-password
export JWT_SECRET=$(openssl rand -base64 32)
export ADMIN_PASSWORD=choose-an-admin-password
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Flyway creates the tables on startup. When the database is empty, the app adds a few languages, a tutor and the admin account. The API runs on <http://localhost:8080>.

### 3. Front end

```bash
cd frontend
npm ci
npm run dev
```

Open <http://localhost:5173>. Log in as `admin@fontys.nl` with your `ADMIN_PASSWORD` to reach the dashboard, or sign up as a learner or tutor to use the chat.

The front end calls the API on `http://localhost:8080`, and the back end only accepts browser requests from `http://localhost:5173` (CORS), so keep both ports as they are.

## Tests and code quality

### Back end

```bash
cd backend
./gradlew test                 # 42 test classes; also writes the JaCoCo coverage report
SONAR_TOKEN=<token> ./gradlew sonar   # analysis on a SonarQube server at http://localhost:9000
```

- Unit tests for the tutor, learner, language and auth use cases, the converters and the exceptions (JUnit 5 + Mockito).
- Controller tests, security tests (token encoding and decoding, the authentication filter, the 401 entry point) and a test for the global exception handler.
- Repository tests against an in-memory H2 database, so no MySQL server is needed for the tests.
- The coverage report (`build/reports/jacoco/test/html/index.html`) leaves out the `domain` and `persistence` packages, because they hold data classes without logic. That way the report focuses on the code with logic in it.

### Front end

```bash
cd frontend
npm run lint
CYPRESS_ADMIN_PASSWORD=choose-an-admin-password npx cypress run   # needs the back end and front end running
```

The Cypress tests open the home page, log in as the admin and check the navigation to the login, dashboard, sign-up and about pages. `npm run cy:open` starts the interactive Cypress runner.

## Pipeline and Docker

`backend/.gitlab-ci.yml` comes from the original GitLab repository and runs these stages on every push: **build → test → jacoco → sonar → deploy**. The deploy stage builds the Docker image and pushes it to the GitLab container registry. It does not run on GitHub.

To build and run the back-end image yourself:

```bash
cd backend
./gradlew assemble
docker build -t tutortalk-backend .
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Dspring.profiles.active=dev" \
  -e DB_URL=jdbc:mysql://host.docker.internal:3307/tutortalk_db \
  -e DB_USERNAME=tutortalk -e DB_PASSWORD=choose-a-password \
  -e JWT_SECRET=<base64-key> -e ADMIN_PASSWORD=choose-an-admin-password \
  tutortalk-backend
```

## Project status

Finished school project (June 2024), not maintained. I developed the back end and front end in two private GitLab repositories at Fontys. This repository brings the final versions together in one place. Settings that were hardcoded there now come from environment variables.

## Author

Mahmud Huseynov · [LinkedIn](https://www.linkedin.com/in/mahmud-huseynov) · [Portfolio](https://mahmud-huseynov.github.io/)
