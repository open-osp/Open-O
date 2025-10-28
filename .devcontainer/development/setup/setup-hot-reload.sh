#!/bin/bash

# Setup hot reload for JSP/HTML/CSS/JS files
# This script creates symlinks from deployed webapp to source files
# allowing instant refresh without rebuilding

WEBAPP_DIR="/usr/local/tomcat/webapps/oscar"
SOURCE_DIR="/workspace/src/main/webapp"

setup_hot_reload() {
    echo "========================================="
    echo "Setting up hot reload for webapp files..."
    echo "========================================="

    # Check if webapp is deployed
    if [ ! -d "$WEBAPP_DIR" ]; then
        echo "Webapp not yet deployed at $WEBAPP_DIR"
        echo "Skipping hot reload setup"
        return 0
    fi

    # Check if source directory exists
    if [ ! -d "$SOURCE_DIR" ]; then
        echo "Source directory not found at $SOURCE_DIR"
        echo "Skipping hot reload setup"
        return 0
    fi

    # Counter for symlinks created
    link_count=0

    # Create symlinks for all webapp content except WEB-INF
    for item in "$SOURCE_DIR"/*; do
    basename_item=$(basename "$item")
    
    # Skip WEB-INF (contains compiled classes and config that must come from build)
    if [ "$basename_item" = "WEB-INF" ]; then
        echo "Skipping $basename_item (needs compiled artifacts)"
        continue
    fi
    
    target_path="$WEBAPP_DIR/$basename_item"
    
    # Remove existing file/directory
    if [ -e "$target_path" ] || [ -L "$target_path" ]; then
        rm -rf "$target_path"
    fi
    
    # Create symlink to source
    ln -sf "$item" "$target_path"
    echo "Linked $basename_item → source"
    ((link_count++))
    done

    echo "========================================="
    echo "   Hot reload setup complete!"
    echo "   Created $link_count symlink(s)"
    echo ""
    echo "   JSP/HTML/CSS changes now reload instantly!"
    echo "   Just edit files and refresh your browser."
    echo "========================================="
}

setup_hot_reload