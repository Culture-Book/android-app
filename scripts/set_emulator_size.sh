#!/bin/bash

# Set the desired screen size
SCREEN_SIZE="1090x2340"
LCD_HEIGHT="1090"
LCD_WIDTH="2340"

# Find all config.ini files in the directory
CONFIG_FILES=$(find "$HOME/.android/avd/" -type f -name "*.ini")

for CONFIG_FILE in $CONFIG_FILES; do
  echo "configuring: $CONFIG_FILE"
  # Replace the entire line with the new screen size
  sed -i '' "s|^skin.name.*=.*|skin.name=$SCREEN_SIZE|g" "$CONFIG_FILE"

  # Replace the entire line with the new LCD height
  sed -i '' "s|^hw.lcd.height.*=.*|hw.lcd.height=$LCD_HEIGHT|g" "$CONFIG_FILE"

  # Replace the entire line with the new LCD width
  sed -i '' "s|^hw.lcd.width.*=.*|hw.lcd.width=$LCD_WIDTH|g" "$CONFIG_FILE"
done