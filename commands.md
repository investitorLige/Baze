# Commands

## Run App With Empty Database

```bash
docker compose up --build mysql app
```

## Seed Database With Fake Data

```bash
docker compose --profile seed up seed-db
```

This runs `sql/seed-random-data.sql` and inserts 50 fake rows per table using seed IDs `10001-10050`.

Seed login example:

```text
username: luka_seed_1
password: seed123
```

## Run App After Seeding

```bash
docker compose up --build mysql app
```

## Stop Containers

```bash
docker compose down
```

This stops/removes containers but keeps Docker volumes, including the database data.

## Linux GUI Permission

If the Swing app cannot open from Docker on Linux, run:

```bash
xhost +local:docker
```

Then start the app again.
