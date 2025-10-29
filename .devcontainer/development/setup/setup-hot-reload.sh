#!/bin/bash
SOURCE_DIR="/workspace/src/main/webapp"
DEPLOY_DIR="/usr/local/tomcat/webapps/oscar"
LOG_FILE="/tmp/webapp-watcher.log"

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

# Watch for file changes
inotifywait -m -r -e close_write,moved_to,delete \
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
  
  # Handle DELETE events
  if [[ "$events" =~ "DELETE" ]]; then
    if [ -f "$DEST_FILE" ] || [ -d "$DEST_FILE" ]; then
      rm -rf "$DEST_FILE"
      echo "[$(date +'%H:%M:%S')] Deleted: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    fi
  # Handle file updates (close_write or moved_to)
  elif [ -f "$SOURCE_FILE" ]; then
    mkdir -p "$(dirname "$DEST_FILE")"
    if cp "$SOURCE_FILE" "$DEST_FILE" 2>/dev/null; then
      echo "[$(date +'%H:%M:%S')] Updated: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    else
      echo "[$(date +'%H:%M:%S')] ERROR: Failed to copy $RELATIVE_PATH$filename" >> "$LOG_FILE"
    fi
  # Handle directory creation
  elif [ -d "$SOURCE_FILE" ]; then
    mkdir -p "$DEST_FILE"
    echo "[$(date +'%H:%M:%S')] Created dir: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
  fi
done