#!/bin/bash
set -euo pipefail

RED="\033[91m"
YELLOW="\033[93m"
GREEN="\033[92m"
BLUE="\033[94m"
RESET="\033[0m"

VIVADO_DIR=$(dirname "$0")
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BUILD_DIR="$VIVADO_DIR/build/${TIMESTAMP}"
LOG="$BUILD_DIR/vivado.log"

mkdir -p "$BUILD_DIR"
echo "Build directory: $BUILD_DIR"

cp $VIVADO_DIR/../src/generated/top.v $BUILD_DIR/top.v
cp $VIVADO_DIR/Cmod-A7-Master.xdc $BUILD_DIR/Cmod-A7-Master.xdc

vivado -mode batch -source "$VIVADO_DIR/build.tcl" -log "$LOG" -nojournal -tclargs "$BUILD_DIR"

echo -e "${BLUE}=== Log summary ===${RESET}"

# ---- Errors / Critical Warnings ----
echo -e "${RED}=== ERRORS ===${RESET}"
if grep -i -E 'ERROR:|CRITICAL WARNING:' "$LOG"; then
    exit 1
fi
echo -e ""

# ---- Warnings ----
echo -e "${YELLOW}=== WARNINGS ===${RESET}"

WARNINGS=$(grep -i 'WARNING:' "$LOG" || true)
while read -r waiver; do
    WARNINGS=$(echo "$WARNINGS" | grep -v "$waiver" || true)
done < $VIVADO_DIR/waiver.txt

if [ -n "$WARNINGS" ]; then
    echo $WARNINGS
    exit 1
fi

echo -e "${GREEN}SUCCESS: Build clean${RESET}"
exit 0
