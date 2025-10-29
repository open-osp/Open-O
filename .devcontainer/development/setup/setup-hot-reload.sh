#!/bin/bash

# Configuration
SOURCE_DIR="/workspace/src/main/webapp"
DEPLOY_DIR="/usr/local/tomcat/webapps/oscar"
LOG_FILE="/tmp/webapp-watcher.log"
PID_FILE="/tmp/webapp-watcher.pid"
INOTIFY_PID=""

# Cleanup function for graceful shutdown
cleanup() {
  echo "" | tee -a "$LOG_FILE"
  echo "[$(date +'%H:%M:%S')] Shutting down hot-reload watcher..." | tee -a "$LOG_FILE"
  
  # Cleanup inotify PID file
  if [[ -n "$INOTIFY_PID" ]]; then
    kill "$INOTIFY_PID" 2>/dev/null
    echo "[$(date +'%H:%M:%S')] Stopped file monitoring (PID: $INOTIFY_PID)" | tee -a "$LOG_FILE"
  fi

  # Clean up script PID file
  if [ -f "$PID_FILE" ]; then
    rm -f "$PID_FILE"
    echo "[$(date +'%H:%M:%S')] Removed PID file" | tee -a "$LOG_FILE"
  fi
  
  echo "=========================================" | tee -a "$LOG_FILE"
  exit 0
}

# Set up signal handlers
trap cleanup INT TERM EXIT

# Main file processing logic
process_file_event() {
  local directory="$1"
  local events="$2"
  local filename="$3"
  
  # Skip if it's a WEB-INF/classes or WEB-INF/lib change
  if [[ "$directory" == "$SOURCE_DIR/WEB-INF/classes" ]] || [[ "$directory" == "$SOURCE_DIR/WEB-INF/lib" ]]; then
    return
  fi
  
  # Make sure changes are inside of source directory
  if [[ ! "$directory" == "$SOURCE_DIR"* ]]; then
    echo "[$(date +'%H:%M:%S')] Ignoring change outside source directory: $directory" >> "$LOG_FILE"
    return
  fi
  
  # Calculate relative path
  local RELATIVE_PATH="${directory#$SOURCE_DIR}"
  RELATIVE_PATH="${RELATIVE_PATH#/}"
  
  local SOURCE_FILE="$directory$filename"
  local DEST_FILE="$DEPLOY_DIR/$RELATIVE_PATH$filename"
  
  # Handle DELETE events
  if grep -qw "DELETE" <<< "$events"; then
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
  else
    # Handle a missing source
    echo "[$(date +'%H:%M:%S')] WARN: Source is missing for event: $RELATIVE_PATH$filename (events: $events)" >> "$LOG_FILE"
  fi
}

# Main execution
main() {
  echo "=========================================" | tee -a "$LOG_FILE"
  echo "[$(date +'%H:%M:%S')] Starting webapp file watcher..." | tee -a "$LOG_FILE"
  echo "Source: $SOURCE_DIR" | tee -a "$LOG_FILE"
  echo "Deploy: $DEPLOY_DIR" | tee -a "$LOG_FILE"
  echo "Log:    $LOG_FILE" | tee -a "$LOG_FILE"
  echo "=========================================" | tee -a "$LOG_FILE"
  echo "" | tee -a "$LOG_FILE"
  
  # Wait for initial deployment
  while [ ! -d "$DEPLOY_DIR" ]; do
    echo "Waiting for webapp deployment..." | tee -a "$LOG_FILE"
    sleep 2
  done
  
  echo "Webapp deployed, watching for changes..." | tee -a "$LOG_FILE"
  echo "" | tee -a "$LOG_FILE"
  
  # Start file monitoring in background
  inotifywait -m -r -e close_write,moved_to,delete \
    --exclude '(\.git|\.svn|node_modules|target|\.class$|WEB-INF/classes|WEB-INF/lib)' \
    "$SOURCE_DIR" 2>> "$LOG_FILE" |
  while read -r directory events filename; do
    process_file_event "$directory" "$events" "$filename"
  done &
  
  # Capture the PID of the background pipeline
  INOTIFY_PID=$!
  echo "[$(date +'%H:%M:%S')] File monitoring started (PID: $INOTIFY_PID)" | tee -a "$LOG_FILE"
  
  # Wait for the background process (keeps script running)
  wait $INOTIFY_PID
}

# Run main function
main
