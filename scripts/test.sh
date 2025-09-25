#!/bin/bash
set -euo pipefail
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC_DIR="$PROJECT_ROOT/src"
TEST_DIR="$PROJECT_ROOT/test"
BUILD_DIR="$PROJECT_ROOT/out/classes"
TEST_BUILD_DIR="$PROJECT_ROOT/out/test-classes"
REPORT_DIR="$PROJECT_ROOT/out/coverage"
JUNIT_JAR="$PROJECT_ROOT/lib/junit-platform-console-standalone-1.10.2.jar"
JACOCO_AGENT="$PROJECT_ROOT/lib/org.jacoco.agent-0.8.11.jar"
JACOCO_CLI="$PROJECT_ROOT/lib/org.jacoco.cli-0.8.11-nodeps.jar"

rm -rf "$BUILD_DIR" "$TEST_BUILD_DIR" "$REPORT_DIR" "$PROJECT_ROOT/out/jacoco.exec"
mkdir -p "$BUILD_DIR" "$TEST_BUILD_DIR" "$REPORT_DIR"

if ! command -v javac >/dev/null 2>&1 || ! javac -version >/dev/null 2>&1; then
    echo "javac not available. Install JDK 21 or update PATH." >&2
    exit 1
fi

if ! command -v java >/dev/null 2>&1 || ! java -version >/dev/null 2>&1; then
    echo "java runtime not available. Install JDK 21 or update PATH." >&2
    exit 1
fi

# Compile main sources
find "$SRC_DIR" -name "*.java" > "$PROJECT_ROOT/out/sources.list"
if [ -s "$PROJECT_ROOT/out/sources.list" ]; then
    javac -d "$BUILD_DIR" @"$PROJECT_ROOT/out/sources.list"
fi

# Compile test sources (expect under test/java)
find "$TEST_DIR" -name "*.java" > "$PROJECT_ROOT/out/tests.list"
if [ -s "$PROJECT_ROOT/out/tests.list" ]; then
    javac -d "$TEST_BUILD_DIR" -cp "$JUNIT_JAR:$BUILD_DIR" @"$PROJECT_ROOT/out/tests.list"
else
    echo "No test sources found" >&2
    exit 1
fi

# Run tests with JaCoCo agent attached
java -javaagent:"$JACOCO_AGENT"=destfile="$PROJECT_ROOT/out/jacoco.exec",includes=hk.edu.polyu.comp.comp2021.clevis.* \
    -jar "$JUNIT_JAR" \
    --class-path "$BUILD_DIR:$TEST_BUILD_DIR" \
    --scan-class-path "$TEST_BUILD_DIR"

# Generate coverage reports
java -jar "$JACOCO_CLI" report "$PROJECT_ROOT/out/jacoco.exec" \
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
