#!/bin/sh
sbt "; clean; flywayMigrate; it:test; test"