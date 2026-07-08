#!/bin/bash

./gradlew test jacocoTestReport

REPORT="$(pwd)/build/reports/jacoco/test/html/index.html"

open-browser "$REPORT"
