docker-compose run --rm mvn versions:set -DnewVersion=0.0.1
docker-compose run --rm mvn clean release:prepare deploy -DpushChanges=false
