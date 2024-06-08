# house-seeker-services

## Workflow Status

| Workflow                 | Status                                                                                                                                                                                                                              |
|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Unit tests               | [![Java unit tests](https://github.com/thiagogb/house-seeker-services/actions/workflows/java-unit-tests.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/java-unit-tests.yml)                    |
| Integration tests        | [![Integration tests](https://github.com/thiagogb/house-seeker-services/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/integration-tests.yml)              |
| Dependency Check         | [![Java dependency check](https://github.com/thiagogb/house-seeker-services/actions/workflows/dependency-check.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/dependency-check.yml)            |
| SonarCloud scanner       | [![SonarCloud scanner](https://github.com/thiagogb/house-seeker-services/actions/workflows/sonar-cloud-scanner.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/sonar-cloud-scanner.yml)         |
| Docker build and publish | [![Docker build and publish](https://github.com/thiagogb/house-seeker-services/actions/workflows/docker-build-publish.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/docker-build-publish.yml) |
| Trivy image scanner      | [![Trivy image scanner](https://github.com/thiagogb/house-seeker-services/actions/workflows/trivy-image-scanner.yml/badge.svg)](https://github.com/thiagogb/house-seeker-services/actions/workflows/trivy-image-scanner.yml)        |

## Code Coverage Report by Module

| Module            | UT: Coverage                                                      | UT: Branches                                                      | IT: Coverage                                            | IT: Branches                                            |
|-------------------|-------------------------------------------------------------------|-------------------------------------------------------------------|---------------------------------------------------------|---------------------------------------------------------|
| Commons           | ![Coverage](.github/badges/commons-coverage-jacoco.svg)           | ![Branches](.github/badges/commons-branches-jacoco.svg)           |                                                         |                                                         |
| Commons Rest      | ![Coverage](.github/badges/commons-rest-coverage-jacoco.svg)      | ![Branches](.github/badges/commons-rest-branches-jacoco.svg)      |                                                         |                                                         |
| Commons Scraper   | ![Coverage](.github/badges/commons-scraper-coverage-jacoco.svg)   | ![Branches](.github/badges/commons-scraper-branches-jacoco.svg)   |                                                         |                                                         |
| Commons Messaging | ![Coverage](.github/badges/commons-messaging-coverage-jacoco.svg) | ![Branches](.github/badges/commons-messaging-branches-jacoco.svg) |                                                         |                                                         |
| Commons GRPC      | ![Coverage](.github/badges/commons-grpc-coverage-jacoco.svg)      | ![Branches](.github/badges/commons-grpc-branches-jacoco.svg)      |                                                         |                                                         |
| Jetimob Scraper   | ![Coverage](.github/badges/jetimob-scraper-coverage-jacoco.svg)   | ![Branches](.github/badges/jetimob-scraper-branches-jacoco.svg)   |                                                         |                                                         |
| Data              | ![Coverage](.github/badges/data-coverage-jacoco.svg)              | ![Branches](.github/badges/data-branches-jacoco.svg)              | ![Coverage](.github/badges/qa-data-coverage-jacoco.svg) | ![Branches](.github/badges/qa-data-branches-jacoco.svg) |
| API               | ![Coverage](.github/badges/api-coverage-jacoco.svg)               | ![Branches](.github/badges/api-branches-jacoco.svg)               | ![Coverage](.github/badges/qa-api-coverage-jacoco.svg)  | ![Branches](.github/badges/qa-api-branches-jacoco.svg)  |
