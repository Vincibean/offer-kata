#!/bin/sh
sbt "; clean; flywayMigrate; run"