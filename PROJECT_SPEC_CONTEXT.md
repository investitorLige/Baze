# Project Spec Context

Source: `Specifikacija projekta (Deo 1).pdf`

## Topic

The project is a Java GUI application connected to a local MySQL database for tracking laboratory experiments.

Core domain entities:
- Laboratories: name and location description.
- Resources: name and optional description/properties.
- Laboratory inventory: many-to-many between laboratories and resources, with quantity or status tracked per laboratory.
- Tools: belong to one laboratory, with identifier, type, purchase date, and optional production date.
- Tool types: name and description.
- Researchers: basic data plus qualifications/abilities.
- Researchers may be experiment designers, experiment performers, or both.
- Experiments: name, required resources/tools, one or more designers, goals/theoretical framework.
- Theories: name, identifying data, and description.
- Experiment executions: happen in laboratories, have performer team, date, and status.
- Execution statuses: planned, started, cancelled, successfully finished, unsuccessfully finished.
- Sessions: scheduled for executions, have date, start time, and end time.
- Sessions in the same laboratory must not overlap.
- After a session ends, used resources/tools are recorded and laboratory inventory is automatically updated.

## Additional Theme Specification

Each team gets a subtheme and must define concrete functional requirements for:
- Experiment descriptions.
- Laboratories, resources, and tools.
- Researchers, including designers and performers.
- Experiment executions.
- Experiment sessions.

Possible subthemes include chemistry, physics, biology, geology, astronomy/astrophysics, electrical engineering, mechanical engineering, psychology, economics, sociology, pedagogy/education, pharmacology, clinical research, neuroscience, and cognitive science.

This repository targets psychology experiments.

## Implemented Database Schema Baseline

The active database name is `psihologija_lab` with `utf8mb4` / `utf8mb4_unicode_ci`.

The application should create the project schema automatically when it first connects to the database, using the supplied 25-table relational model:
`LABORATORIJA`, `RESURS`, `TIP_ALATA`, `ISTRAZIVAC`, `TEORIJA`, `EKSPERIMENT`, `UPITNIK`, `ETICKI_ODBOR`, `ALAT`, `IZVODJENJE`, `SESIJA`, `ISPITANIK`, `PROTOKOL`, `KVALIFIKACIJA`, `ODOBRENJE`, `POTREBAN_RESURS`, `POTREBAN_ALAT`, `KORISCEN_UPITNIK`, `INVENTAR_LABORATORIJE`, `DIZAJNER`, `TIM_IZVODJACA`, `UPOTREBA_RESURSA`, `UPOTREBA_ALATA`, `UCESCE`, and `REZULTAT_UPITNIKA`.

The supplied database objects include view `pregled_eksperimenata` and procedure `zakazi_sesiju`. `pregled_eksperimenata` joins experiments, theories, designers, executions, sessions, and attendance with `GROUP BY` and `HAVING`. `zakazi_sesiju` uses a transaction to prevent overlapping sessions in the same laboratory.

The Java login/register implementation keeps `korisnicko_ime` in `ISTRAZIVAC` and stores login credentials in `src/main/resources/users.txt` as `korisnicko_ime:password`. The database schema should not add a `password` column to `ISTRAZIVAC`.

## Local Project Direction

This application is implemented only from the researcher's perspective. Every interactive user is an `ISTRAZIVAC`; there are no administrator or external-user workflows in the app UI. Other users, supporting entities, and lookup/reference tables are treated as already populated automatically through seed data or database setup, not through separate application roles.

All database table names, column names, Java model fields that mirror database attributes, and visible form labels should use Serbian names from the relational schema. Do not invent English field names when the schema already provides Serbian names.

## Database Requirements

The database must contain:
- Data about laboratories, resources, and tools.
- Data about researchers.
- Data about experiments and sessions.
- Data from the additional theme specification.

Every table must have a primary key and applicable foreign keys.

Each table should have at least 100 rows, except tables where that is not meaningful.

The database must also include:
- At least one view with at least two joins, mandatory `GROUP BY`, and `HAVING`.
- At least one procedure with a transaction, at least two non-`SELECT` operations, one `SELECT`, and at least one local variable that is declared, set, and used.
- At least one function with at least one input parameter.
- One additional no-parameter function that tests the previous function for at least five inputs and returns `TRUE` or `FALSE`.

Each additional database object, except the test function, must include a comment explaining its purpose and why it is introduced.

User login data must also be stored in a separate text file. For each user, store username and password.

## Application Requirements

Independent forms required for every subtheme:
- Register form / sign up.
- Login form / log in.

Researcher perspective forms:
- View planned and completed experiments.
- Change the status of a specific experiment.
- Delete a session, allowed only for researchers participating in the experiment for that session.

Administrator perspective forms:
- View scheduled sessions and the experiments executed in them.
- Change data for a scheduled session.
- Delete a laboratory, allowed only if no researcher works in that laboratory.

External user perspective forms:
- View laboratories and researchers in those laboratories.
- Update own username and password.
- Delete own account, allowed only if the user enters the correct password. The password is sent to the database for verification.

## Independent Queries

Each team member must design and implement an additional read-only query over the additional theme specification.

Team members cannot have the same query or a query with the same functionality.

## Technical Requirements

- Database must run on a local server launched from phpMyAdmin or MySQL Workbench.
- Application must be written in Java and have a GUI.
- GUI appearance is not graded, but it must exist, be functional, and expose the required forms.
- Queries are graded only if testable through the GUI.
- JDBC must be used and included as a dependency.
- Use `PreparedStatement` for every parameterized query.
- Use `Statement` for non-parameterized queries.
- Queries must be written entirely in SQL. Splitting query logic between SQL and Java code is not accepted.

## Submission Requirements

Submit:
- Additional theme specification document.
- Relational schema in a text file.
- SQL file for creating and populating the database.
- Java project archive with JDBC dependency and user accounts text file.
- Separate `.sql` file for each team member's independent query, named after that member.

## Grading

Maximum: 30 points.

Breakdown:
- Additional theme specification: 3
- Database model: 7
- Database creation/population and supporting text files: 2
- Additional database objects: 7
- Additional query per team member: 3
- GUI: 2
- JDBC connection: 1
- SQL queries and Java integration through JDBC: 5
