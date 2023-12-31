#!/usr/bin/python

'''Samba Apport interface

Copyright (C) 2010 Canonical Ltd/
Author: Chuck Short <chuck.short@canonical.com>

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2 of the License, or (at your
option) any later version.  See http://www.gnu.org/copyleft/gpl.html for
the full text of the license.
'''

import os
import subprocess
from apport.hookutils import *


def recent_smblog(pattern):
	'''Extract recent messages from log.smbd or messages which match a regex
	   pattern should be a "re" object. '''
	lines = ''
	if os.path.exists('/var/log/samba/log.smbd'):
		file = '/var/log/samba/log.smbd'
	else:
		return lines

	for line in open(file):
		if pattern.search(line):
			lines += line
	return lines

def recent_nmbdlog(pattern):
	''' Extract recent messages from log.nmbd or messages which match regex
	    pattern should be a "re" object. '''
	lines = ''
	if os.path.exists('/var/log/samba/log.nmbd'):
		file = '/var/log/samba/log.nmbd'
	else:
		return lines

	for line in open(file):
		if pattern.search(line):
			lines += line
	return lines

def add_info(report, ui):
	packages = ['samba', 'samba-common-bin', 'samba-common', 'samba-tools', 'smbclient', 'swat',
		'samba-doc', 'samba-doc-pdf', 'smbfs', 'libpam-smbpass', 'libsmbclient', 'libsmbclient-dev',
		'winbind', 'samba-dbg', 'libwbclient0']

	versions = ''
	for package in packages:
		try:
			version = packaging.get_version(package)
		except ValueError:
			version = 'N/A'
		if version is None:
			version = 'N/A'
		versions += '%s %s\n' %(package, version)
	report['SambaInstalledVersions'] = versions


	# Interactive report
	ui.information("As a part of the bug reporting process, you'll be asked as series of questions to help provide a more descriptive bug report. Please answer the following questions to the best of your abilities. Afterwards, a browser will be opened to finish filing this as a bug in the Launchpad bug tracking system.")

	response = ui.choice("How would you best describe your setup?", ["I am running a Windows File Server.", "I am connecting to a Windows File Server."], False)
	
	if response == None:
		raise StopIteration # user has canceled
	elif response[0] == 0: #its a server
		response = ui.yesno("Did this used to work properly with a previous release?")
		if response == None: # user has canceled
			raise StopIteration
		if response == False:
			report['SambaServerRegression'] = "No"
		if response == True:
			report['SambaServerRegression'] = 'Yes'
	
		response = ui.choice("Which clients are failing to connect?", ["Windows", "Ubuntu", "Both", "Other"], False)
		if response == None:
			raise StopIteration # user has canceled
		if response[0] == 0:
			report['UbuntuFailedConnect'] = 'Yes'
		if response[0] == 1:
			report['WindowsFailedConnect'] = 'Yes'
		if response[0] == 2:
			report['BothFailedConnect']  = 'Yes'
		if repsonse[0] == 3:
			report['OtherFailedconnect'] = 'Yes'

		response = ui.yesno("The contents of your /etc/samba/smb.conf file may help developers diagnose your bug more quickly. However, it may contain sensitive information.  Do you want to include it in your bug report?")
		if response == None:
			raise StopIteration
		if response == False:
			report['SmbConfIncluded'] = 'No'
		if response == True:
			report['SmbConfInclude'] = 'Yes'
			attach_file_if_exists(report, '/etc/samba/smb.conf', key='SMBConf')

		response = ui.yesno("The contents of your /var/log/samba/log.smbd and /var/log/samba/log.nmbd may help developers diagnose your bug more quickly. However, it may contain sensitive information. Do you want to include it in your bug report?")
		if response == None:
			raise StopIteration
		elif response == False:
			ui.information("The contents of your /var/log/samba/log.smbd and /var/log/samba/log.nmbd will NOT be included in the bug report.")
		elif response == True:
			sec_re = re.compile('failed', re.IGNORECASE)
			report['SmbLog'] = recent_smblog(sec_re)
			report['NmbdLog'] = recent_nmbdlog(sec_re)

	elif response[0] == 1: #its a client
		response = ui.yesno("Did this used to work properly with a previous release?")
		if response == None: #user has canceled
			raise StopIteration
		if response == False:
			report['SambaClientRegression'] = "No"
		if response == True:
			report['SambaClientRegression'] = "Yes"

		response = ui.choice("How is the remote share accessed from the Ubuntu system?", ["Nautilus (or other GUI Client)", "smbclient (from the command line)", "cifs filesystem mount (from /etc/fstab or a mount command)"], False)
		if response == None: #user has canceled
			raise StopIteration
		if response[0] == 0:
			attach_related_packages(report, ['nautilus', 'gvfs'])
		if response[0] == 1:
			ui.information("Please attach the output of 'smbclient -L localhost' to the end of this bug report.")
		if response[0] == 2:
			report['CIFSMounts'] = command_output(['mount', '|', 'grep', 'cifs'])
			if os.path.exists('/proc/fs/cifs/DebugData'):
				report['CifsVersion'] = command_output(['cat', '/proc/fs/cifs/DebugData'])

	ui.information("After apport finishes collecting information, please document your steps to reproduce the issue when filling out the bug report.")
