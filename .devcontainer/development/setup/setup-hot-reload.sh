#!/bin/bash
SOURCE_DIR="/workspace/src/main/webapp"
DEPLOY_DIR="/usr/local/tomcat/webapps/oscar"
LOG_FILE="/tmp/webapp-watcher.log"
DEBOUNCE_FILE="/tmp/webapp-debounce"

echo "=========================================" | tee -a "$LOG_FILE"
echo "Starting webapp file watcher..." | tee -a "$LOG_FILE"
echo "Source: $SOURCE_DIR" | tee -a "$LOG_FILE"
echo "Deploy: $DEPLOY_DIR" | tee -a "$LOG_FILE"
echo "=========================================" | tee -a "$LOG_FILE"

# Wait for initial deployment
while [ ! -d "$DEPLOY_DIR" ]; do
  echo "Waiting for webapp deployment..." | tee -a "$LOG_FILE"
  sleep 2
done

echo "Webapp deployed, watching for changes..." | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

# Initialize debounce tracking file
mkdir -p "$(dirname "$DEBOUNCE_FILE")"
> "$DEBOUNCE_FILE"

# Watch for file changes
inotifywait -m -r -e modify,close_write,create,delete,move \
  --exclude '(\.git|\.svn|node_modules|target|\.class$|WEB-INF/classes|WEB-INF/lib)' \
  "$SOURCE_DIR" 2>> "$LOG_FILE" |
while read -r directory events filename; do
  # Skip if it's a WEB-INF/classes or WEB-INF/lib change
  if [[ "$directory" =~ WEB-INF/classes ]] || [[ "$directory" =~ WEB-INF/lib ]]; then
    continue
  fi
  
  # Calculate relative path
  RELATIVE_PATH="${directory#$SOURCE_DIR}"
  RELATIVE_PATH="${RELATIVE_PATH#/}"
  
  SOURCE_FILE="$directory$filename"
  DEST_FILE="$DEPLOY_DIR/$RELATIVE_PATH$filename"
  FILE_KEY="$RELATIVE_PATH$filename"
  CURRENT_TIME=$(date +%s)
  
  # Simple file-based debounce check (last 2 seconds)
  LAST_SYNC=$(grep -F "$FILE_KEY" "$DEBOUNCE_FILE" 2>/dev/null | cut -d: -f2)
  LAST_SYNC=${LAST_SYNC:-0}
  TIME_DIFF=$((CURRENT_TIME - LAST_SYNC))
  
  # Handle DELETE events (always process)
  if [[ "$events" =~ "DELETE" ]]; then
    if [ -f "$DEST_FILE" ]; then
      rm -f "$DEST_FILE"
      echo "[$(date +'%H:%M:%S')] Deleted: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
      # Remove from debounce file
      grep -vF "$FILE_KEY" "$DEBOUNCE_FILE" > "${DEBOUNCE_FILE}.tmp" 2>/dev/null || true
      mv "${DEBOUNCE_FILE}.tmp" "$DEBOUNCE_FILE" 2>/dev/null || true
    fi
  # Handle file updates with debounce
  elif [ -f "$SOURCE_FILE" ] && [ $TIME_DIFF -gt 2 ]; then
    mkdir -p "$(dirname "$DEST_FILE")"
    cp "$SOURCE_FILE" "$DEST_FILE"
    echo "[$(date +'%H:%M:%S')] Updated: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    # Update debounce tracking
    grep -vF "$FILE_KEY" "$DEBOUNCE_FILE" > "${DEBOUNCE_FILE}.tmp" 2>/dev/null || true
    echo "$FILE_KEY:$CURRENT_TIME" >> "${DEBOUNCE_FILE}.tmp"
    mv "${DEBOUNCE_FILE}.tmp" "$DEBOUNCE_FILE"
  # Handle directory creation
  elif [ -d "$SOURCE_FILE" ]; then
    mkdir -p "$DEST_FILE"
    echo "[$(date +'%H:%M:%S')] Created dir: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
  fi
  
  # Periodic cleanup of debounce file (every 50 events)
  if [ $((RANDOM % 50)) -eq 0 ]; then
    # Remove entries older than 60 seconds
    CUTOFF=$((CURRENT_TIME - 60))
    awk -F: -v cutoff="$CUTOFF" '$2 > cutoff' "$DEBOUNCE_FILE" > "${DEBOUNCE_FILE}.tmp" 2>/dev/null || true
    mv "${DEBOUNCE_FILE}.tmp" "$DEBOUNCE_FILE" 2>/dev/null || true
  fi
done