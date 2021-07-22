# Blocklist Tools

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=blocklist-tools_blocklist-tools&metric=coverage)](https://sonarcloud.io/dashboard?id=blocklist-tools_blocklist-tools)

Java library for working with blocklists.

* Parse hosts and domain formatted blocklists. Hopefully support for more formats in the future (PRs welcome!)
* Compare lists by generating a sorted de-duplicated diff.
* Preform DNS queries

Versions: https://search.maven.org/artifact/com.developerdan/blocklist-tools

### Cut a new release

    docker-compose run --rm mvn versions:set -DnewVersion=0.0.1
    docker-compose run --rm mvn clean release:prepare deploy -DpushChanges=false
