#!/bin/bash

./gradlew dokkaHtml

DOCS="$(pwd)/build/dokka/html/index.html"

open-browser "$DOCS"
