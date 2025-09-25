#!/bin/bash
# @author FA, Haochen 24113347D w/ AI Support
# @date_created 26th Sep, 2025
# @latest_update 26th Sep, 2025 by FA, Haochen
# @description JaCoCo & JUnit Unit Test Entry Script
set -euo pipefail
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC_DIR="$PROJECT_ROOT/src"
TEST_DIR="$PROJECT_ROOT/test"
BUILD_DIR="$PROJECT_ROOT/out/classes"
TEST_BUILD_DIR="$PROJECT_ROOT/out/test-classes"
REPORT_DIR="$PROJECT_ROOT/out/coverage"
SOURCES_LIST="$PROJECT_ROOT/out/sources.list"
TESTS_LIST="$PROJECT_ROOT/out/tests.list"
JUNIT_JAR="$PROJECT_ROOT/lib/junit-platform-console-standalone-1.10.2.jar"
JACOCO_AGENT="$PROJECT_ROOT/lib/org.jacoco.agent-0.8.11-runtime.jar"
JACOCO_CLI="$PROJECT_ROOT/lib/org.jacoco.cli-0.8.11-nodeps.jar"
MAIN_COMPILE_OPTS=(--release 21 -d "$BUILD_DIR")
TEST_COMPILE_OPTS=(--release 21 -d "$TEST_BUILD_DIR")

if [ -z "${JAVA_HOME:-}" ]; then
	if command -v /usr/libexec/java_home >/dev/null 2>&1; then
		JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null || /usr/libexec/java_home 2>/dev/null || true)
	fi
fi

JAVAC=${JAVA_HOME:+"$JAVA_HOME/bin/javac"}
JAVA=${JAVA_HOME:+"$JAVA_HOME/bin/java"}

if [ -z "$JAVAC" ] || [ ! -x "$JAVAC" ]; then
	JAVAC=$(command -v javac 2>/dev/null || true)
fi

if [ -z "$JAVA" ] || [ ! -x "$JAVA" ]; then
	JAVA=$(command -v java 2>/dev/null || true)
fi

rm -rf "$BUILD_DIR" "$TEST_BUILD_DIR" "$REPORT_DIR" "$PROJECT_ROOT/out/jacoco.exec"
mkdir -p "$BUILD_DIR" "$TEST_BUILD_DIR" "$REPORT_DIR"

if [ -z "$JAVAC" ] || ! "$JAVAC" -version >/dev/null 2>&1; then
	echo "javac not available. Install JDK 21 or update PATH." >&2
	exit 1
fi

if [ -z "$JAVA" ] || ! "$JAVA" -version >/dev/null 2>&1; then
	echo "java runtime not available. Install JDK 21 or update PATH." >&2
	exit 1
fi

# Compile main sources with Java 21 target to satisfy JaCoCo tooling
find "$SRC_DIR" -name "*.java" >"$SOURCES_LIST"
if [ -s "$SOURCES_LIST" ]; then
	"$JAVAC" "${MAIN_COMPILE_OPTS[@]}" @"$SOURCES_LIST"
fi

# Compile test sources (expect under test/java)
find "$TEST_DIR" -name "*.java" >"$TESTS_LIST"
if [ -s "$TESTS_LIST" ]; then
	"$JAVAC" "${TEST_COMPILE_OPTS[@]}" -cp "$JUNIT_JAR:$BUILD_DIR" @"$TESTS_LIST"
else
	echo "No test sources found" >&2
	exit 1
fi

# Run tests with JaCoCo agent attached
"$JAVA" -javaagent:"$JACOCO_AGENT"=destfile="$PROJECT_ROOT/out/jacoco.exec",includes=hk.edu.polyu.comp.comp2021.clevis.* \
	-jar "$JUNIT_JAR" \
	--class-path "$BUILD_DIR:$TEST_BUILD_DIR" \
	--scan-class-path "$TEST_BUILD_DIR"

# Generate coverage reports
"$JAVA" -jar "$JACOCO_CLI" report "$PROJECT_ROOT/out/jacoco.exec" \
	--classfiles "$BUILD_DIR" \
	--sourcefiles "$SRC_DIR" \
	--html "$REPORT_DIR/html" \
	--xml "$REPORT_DIR/coverage.xml" \
	--csv "$REPORT_DIR/coverage.csv"

cat <<MSG
Test run complete.
- Exec data: out/jacoco.exec
- HTML report: out/coverage/html/index.html
- XML report: out/coverage/coverage.xml
- CSV report: out/coverage/coverage.csv
MSG
