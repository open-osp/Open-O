# Time zone data in Open-O (OSCAR)

Open-O stores and renders wall-clock local times; it carries **no timezone
database of its own**. Correct behaviour after civil-time changes (e.g.
British Columbia's move to permanent UTC-7, effective after the 2026-03-08
spring-forward, modeled in IANA tzdata **2026b**) depends entirely on the
runtime images:

| Layer | Where it lives | BC change requires |
|---|---|---|
| JVM rules | `tzdb.dat` inside the JRE of the runtime image | JRE tzdata ≥ 2026b |
| OS zoneinfo | `/usr/share/zoneinfo` in the runtime image | tzdata ≥ 2026b |
| SQL tz tables | `mysql.time_zone*` in MariaDB | regenerated after OS tzdata update |

As of 2026-09 the published `openosp/open-osp:latest` runtime still ships JVM
tzdb **2024a** (base `tomcat:9.0.102-jre8-temurin-jammy`) and OS tzdata
**2025b** — i.e. it would wrongly fall back to UTC-8 on 2026-11-01.

**No Open-O source change is required** for the BC change. The update path is
image-level, scripted and verified in the [open-osp
repo](https://github.com/open-osp/open-osp): `bin/update-timezones.sh`
(overlay images from `eclipse-temurin:8-jre` / Ubuntu tzdata donors) — see
`doc/timezone-update-runbook.md` there.

Notes for Open-O maintainers:

* If CI asserts timezone-sensitive behaviour, pin the runner's tzdata ≥ 2026b
  or set `TZ`/`user.timezone` expectations accordingly.
* Anything pattern-matching the zone *abbreviation* ("PDT"/"PST") should be
  avoided: per tzdata 2026b modeling, the `America/Vancouver` zone is labeled
  DST until 2026-11-01 even though the UTC-7 offset is already permanent.
