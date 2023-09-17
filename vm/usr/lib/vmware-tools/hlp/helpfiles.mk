#
# This file should be included by any makefile that wants to package the
# Toolbox help files.  Future additions to the files that need to be included
# for the Toolbox html help should only need to modify this file, not each of
# the makefiles including this one.
#

TOOLBOX_HELP_FILES := 
TOOLBOX_HELP_DIRS  :=

TOOLBOX_HELP_FILES += index.htm
TOOLBOX_HELP_FILES += ix.htm
TOOLBOX_HELP_FILES += toc.htm
TOOLBOX_HELP_FILES += tools_about.htm
TOOLBOX_HELP_FILES += tools_controlpanel.htm
TOOLBOX_HELP_FILES += tools_devices.htm
TOOLBOX_HELP_FILES += tools_intro.htm
TOOLBOX_HELP_FILES += tools_options.htm
TOOLBOX_HELP_FILES += tools_scripts.htm
TOOLBOX_HELP_FILES += tools_shrink.htm

TOOLBOX_HELP_IMAGES := index.gif
TOOLBOX_HELP_IMAGES += blank.gif
TOOLBOX_HELP_IMAGES += indexx.gif
TOOLBOX_HELP_IMAGES += next.gif
TOOLBOX_HELP_IMAGES += nextx.gif
TOOLBOX_HELP_IMAGES += prev.gif
TOOLBOX_HELP_IMAGES += prevx.gif
TOOLBOX_HELP_IMAGES += print.gif
TOOLBOX_HELP_IMAGES += toc.gif
TOOLBOX_HELP_IMAGES += tocx.gif

TOOLBOX_HELP_DIRS  += images
TOOLBOX_HELP_FILES += $(foreach image, $(TOOLBOX_HELP_IMAGES), images/$(image))
