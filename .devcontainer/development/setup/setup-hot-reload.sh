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

# Watch for file changes and sync them
inotifywait -m -r -e modify,create,delete,move \
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
  
  # Handle different events
  if [[ "$events" =~ "DELETE" ]]; then
    if [ -f "$DEST_FILE" ]; then
      rm -f "$DEST_FILE"
      echo "[$(date +'%H:%M:%S')] Deleted: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    fi
  elif [[ "$events" =~ "MOVED_TO" ]] || [[ "$events" =~ "CREATE" ]] || [[ "$events" =~ "MODIFY" ]]; then
    if [ -f "$SOURCE_FILE" ]; then
      mkdir -p "$(dirname "$DEST_FILE")"
      cp "$SOURCE_FILE" "$DEST_FILE"
      echo "[$(date +'%H:%M:%S')] Updated: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    elif [ -d "$SOURCE_FILE" ]; then
      mkdir -p "$DEST_FILE"
      echo "[$(date +'%H:%M:%S')] Created dir: $RELATIVE_PATH$filename" | tee -a "$LOG_FILE"
    fi
  fi
done