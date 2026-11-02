on run arguments
    set sourceFile to item 1 of arguments
    set className to item 2 of arguments
    set projectRoot to item 3 of arguments
    set tabTitle to "Java: " & className
    set javaCommand to "export JAVA_HOME=$(/usr/libexec/java_home -v 21); export PATH=\"$JAVA_HOME/bin:$PATH\"; source_dir=$(/usr/bin/dirname " & quoted form of sourceFile & "); clear; mkdir -p /tmp/j26-java && javac --release 21 -d /tmp/j26-java \"$source_dir\"/*.java && java -cp /tmp/j26-java " & quoted form of className

    tell application "Ghostty"
        set targetTab to missing value

        repeat with ghosttyWindow in windows
            repeat with ghosttyTab in tabs of ghosttyWindow
                if name of ghosttyTab is tabTitle then
                    set targetTab to ghosttyTab
                    exit repeat
                end if
            end repeat

            if targetTab is not missing value then exit repeat
        end repeat

        if targetTab is missing value then
            set surfaceConfig to new surface configuration
            set initial working directory of surfaceConfig to projectRoot

            if (count of windows) is 0 then
                set targetWindow to new window with configuration surfaceConfig
                set targetTab to selected tab of targetWindow
            else
                set targetTab to new tab in front window with configuration surfaceConfig
            end if

            perform action ("set_tab_title:" & tabTitle) on focused terminal of targetTab
        else
            select tab targetTab
        end if

        set targetTerminal to focused terminal of targetTab
        focus targetTerminal
        input text (javaCommand & linefeed) to targetTerminal
    end tell
end run
