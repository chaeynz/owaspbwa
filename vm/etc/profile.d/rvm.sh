#
# RVM profile
#
# /etc/profile.d/rvm.sh # sh extension required for loading.
#

if
  [ -n "${BASH_VERSION:-}" -o -n "${ZSH_VERSION:-}" ] &&
  test "`ps -p $$ -o comm=`" != dash &&
  test "`ps -p $$ -o comm=`" != sh
then
  [[ -n "${rvm_stored_umask:-}" ]] || export rvm_stored_umask=$(umask)
  # Load user rvmrc configurations, if exist
  for file in "/etc/rvmrc" "$HOME/.rvmrc"
  do
    [[ -s "$file" ]] && source $file
  done
  if
    [[ -n "${rvm_prefix:-}" ]] &&
    [[ -s "${rvm_prefix}/.rvmrc" ]] &&
    [[ ! "$HOME/.rvmrc" -ef "${rvm_prefix}/.rvmrc" ]]
  then
    source "${rvm_prefix}/.rvmrc"
  fi

  # Load RVM if it is installed, try user then root install.
  if
    [[ -s "$rvm_path/scripts/rvm" ]]
  then
    source "$rvm_path/scripts/rvm"
  elif
    [[ -s "$HOME/.rvm/scripts/rvm" ]]
  then
    true ${rvm_path:="$HOME/.rvm"}
    source "$HOME/.rvm/scripts/rvm"
  elif
    [[ -s "/usr/local/rvm/scripts/rvm" ]]
  then
    true ${rvm_path:="/usr/local/rvm"}
    source "/usr/local/rvm/scripts/rvm"
  fi

  # Opt-in for custom prompt through by setting:
  #     rvm_ps1=1
  # in either /etc/rvmrc or $HOME/.rvmrc
  if
    [[ ${rvm_ps1:-0} -eq 1 ]]
  then
    # Source RVM ps1 functions for a great prompt.
    if
      [[ -s "$rvm_path/contrib/ps1_functions" ]]
    then
      source "$rvm_path/contrib/ps1_functions"
    elif
      [[ -s "/usr/local/rvm/contrib/ps1_functions" ]]
    then
      source "/usr/local/rvm/contrib/ps1_functions"
    fi

    if command -v ps1_set >/dev/null 2>&1
    then ps1_set
    fi
  fi

  # Add $rvm_bin_path to $PATH if necessary
  [[ "${rvm_bin_path}" == "${rvm_path}/bin" || ":${PATH}:" =~ ":${rvm_bin_path}:" ]] ||
    __rvm_add_to_path prepend "${rvm_bin_path}"
fi
