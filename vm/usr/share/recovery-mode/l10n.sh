
# There is no environment set, as these steps are skipped, 
# so we need to source the variables needed for localization ourselves
if [ -r /etc/default/locale ]; then
 . /etc/default/locale
export LANG LANGUAGE
elif [ -r /etc/environment ]; then
 . /etc/environment
export LANG LANGUAGE
fi

# default eval_gettext() to ensure that we do not fail
# if gettext-base is not installed
eval_gettext() {
    echo "$1"
}
. gettext.sh
export TEXTDOMAIN=friendly-recovery
export TEXTDOMAINDIR=/usr/share/locale
