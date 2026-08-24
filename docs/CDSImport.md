# Using ImportDemographicDataCli

## Prerequisites

1. OSCAR must be built — run `mvn package` to produce the WAR
2. Database must be running and reachable with the credentials in `oscar.properties`
3. `oscar.properties` must be accessible — it supplies `DOCUMENT_DIR`, database connection, and other runtime config

## Classpath Setup

The CLI runs against the exploded WAR. Extract or locate it:

```bash
# Explode the WAR (one time)
mkdir -p /opt/oscar-cli
cd /opt/oscar-cli
jar -xf /path/to/oscar.war
```

Your classpath needs two things:

- `WEB-INF/classes/` — compiled application classes
- `WEB-INF/lib/*` — all dependency JARs

The CLI also needs your site's `oscar.properties` for the correct database credentials. There are two ways to supply it.

### Option A — classpath (recommended)

Add the directory containing `oscar.properties` to the classpath. The CLI detects `oscar.properties` on the classpath and loads it automatically, overriding the dev defaults bundled in the WAR.

```bash
java \
  -cp "/opt/oscar-cli/WEB-INF/classes:/opt/oscar-cli/WEB-INF/lib/*:/etc/oscar" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input <path> \
  --provider <providerNo>
```

Where `/etc/oscar` is the directory that contains `oscar.properties`.

### Option B — system property

Pass the absolute file path via `-Doscar_override_properties`:

```bash
java \
  -Doscar_override_properties=/etc/oscar/oscar.properties \
  -cp "/opt/oscar-cli/WEB-INF/classes:/opt/oscar-cli/WEB-INF/lib/*" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input <path> \
  --provider <providerNo>
```

## Running the CLI

```bash
java \
  -cp "/opt/oscar-cli/WEB-INF/classes:/opt/oscar-cli/WEB-INF/lib/*:/etc/oscar" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input <path> \
  --provider <providerNo> \
  [options]
```

## Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `--input <path>` | Yes | Path to a single `.xml` patient file, a `.zip` archive, or a directory of patient folders |
| `--provider <no>` | Yes | Provider number of the user performing the import (e.g. `999`) |
| `--program <id>` | No | Program ID to associate with imported patients. Default: `0` |
| `--timeshift <days>` | No | Shift all appointment/note dates forward by N days. Default: `0` |
| `--no-match-providers` | No | Flag. Disables matching imported providers by name to existing providers |
| `--help` | No | Print usage and exit |

## Input Path Formats

The `--input` argument accepts three formats, matching how the web UI handles imports:

| Format | Description |
|--------|-------------|
| `/data/exports/Smith_John.xml` | Single patient CDS/XML file |
| `/data/exports/batch.zip` | ZIP archive; unzipped in place, then all patient folders are processed |
| `/data/exports/avaros_export/` | Directory where each subdirectory contains one patient's XML + PDFs |

Expected directory structure for a directory import:

```
avaros_export/
  Smith_John/
    Smith_John.xml
    document1.pdf
    document2.pdf
  Jones_Mary/
    Jones_Mary.xml
    report.pdf
```

## Examples

Import a single patient file:

```bash
java -cp "WEB-INF/classes:WEB-INF/lib/*:/etc/oscar" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input /data/exports/Smith_John.xml \
  --provider 999
```

Import a ZIP archive with provider matching disabled:

```bash
java -cp "WEB-INF/classes:WEB-INF/lib/*:/etc/oscar" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input /data/exports/avaros_500_patients.zip \
  --provider 999 \
  --no-match-providers
```

Import a directory, shifting all dates forward 30 days (for a training environment):

```bash
java -cp "WEB-INF/classes:WEB-INF/lib/*:/etc/oscar" \
  oscar.oscarDemographic.pageUtil.ImportDemographicDataCli \
  --input /data/exports/avaros_export/ \
  --provider 999 \
  --program 12 \
  --timeshift 30
```

## Output

On success, the CLI prints:

```
Initializing Spring context...
Starting import from: /data/exports/avaros_export
provider=999  program=0  timeshift=0  matchProviders=true
Import log written to: /data/exports/import_log_20240203.xls
Done.
```

Any import warnings (non-fatal issues like unmatched providers or missing fields) are printed before the log path:

```
=== WARNINGS ===
Provider 'Dr. Smith' not found, skipping match
Missing birth date for patient Jones_Mary
```

The import log is an Excel file written to the parent directory of `--input`. It contains one row per patient with counts of what was imported (demographics, medications, labs, notes, etc.).

## Troubleshooting

| Symptom | Likely cause |
|---------|--------------|
| `NoSuchBeanDefinitionException` at startup | A required bean is missing from `applicationContext.xml` — check that the full WAR classpath is included |
| `Error: provider not found: 999` | The provider number doesn't exist in the database |
| `DOCUMENT_DIR` not writable | Check `oscar.properties` — the path in `DOCUMENT_DIR` must exist and be writable by the process user |
| Spring fails to connect to DB | Verify DB is running; ensure `oscar.properties` is either on the classpath or supplied via `-Doscar_override_properties`. The CLI prints `Loaded file:…oscar.properties` on startup if it found one. |
