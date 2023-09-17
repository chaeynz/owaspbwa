'''Attach generally useful information, not specific to any package.

Copyright (C) 2009 Canonical Ltd.
Author: Matt Zimmerman <mdz@canonical.com>

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2 of the License, or (at your
option) any later version.  See http://www.gnu.org/copyleft/gpl.html for
the full text of the license.
'''

import apport.packaging
import re
from urlparse import urljoin
from urllib2 import urlopen
from apport.hookutils import *

def add_info(report):
    add_tags = []

    # crash reports from live system installer often expose target mount
    for f in ('ExecutablePath', 'InterpreterPath'):
        if f in report and report[f].startswith('/target/'):
            report[f] = report[f][7:]

    # if we are running from a live system, add the build timestamp
    attach_file_if_exists(report, '/cdrom/.disk/info', 'LiveMediaBuild')

    # This includes the Ubuntu packaged kernel version
    attach_file_if_exists(report, '/proc/version_signature', 'ProcVersionSignature')

    # https://wiki.ubuntu.com/FoundationsTeam/Specs/OemTrackingId
    attach_file_if_exists(report, '/var/lib/ubuntu_dist_channel', 
        'DistributionChannelDescriptor')

    # https://bugs.launchpad.net/bugs/364649
    attach_file_if_exists(report, '/var/log/installer/media-info',
                          'InstallationMedia')

    release_codename = apport.hookutils.command_output(['lsb_release', '-sc'])
    if not release_codename.startswith('Error'):
        add_tags.append(release_codename)

    # There are enough of these now that it is probably worth refactoring...
    # -mdz
    if report['ProblemType'] == 'Package':
        if report['Package'] not in ['grub', 'grub2']:
            # linux-image postinst emits this when update-grub fails
            # https://wiki.ubuntu.com/KernelTeam/DebuggingUpdateErrors
            if 'DpkgTerminalLog' in report and re.search(r'^User postinst hook script \[.*update-grub\] exited with value', report['DpkgTerminalLog'], re.MULTILINE):
                # File these reports on the grub package instead
                grub_package = apport.packaging.get_file_package('/usr/sbin/update-grub')
                if grub_package is None or grub_package == 'grub':
                    report['SourcePackage'] = 'grub'
                else:
                    report['SourcePackage'] = 'grub2'

        if report['Package'] != 'initramfs-tools':
            # update-initramfs emits this when it fails, usually invoked from the linux-image postinst
            # https://wiki.ubuntu.com/KernelTeam/DebuggingUpdateErrors
            if 'DpkgTerminalLog' in report and re.search(r'^update-initramfs: failed for ', report['DpkgTerminalLog'], re.MULTILINE):
                # File these reports on the initramfs-tools package instead
                report['SourcePackage'] = 'initramfs-tools'

        if report['Package'] in ['emacs22', 'emacs23', 'emacs-snapshot', 'xemacs21']:
            # emacs add-on packages trigger byte compilation, which might fail
            # we are very interested in reading the compilation log to determine
            # where to reassign this report to
            regex = r'^!! Byte-compilation for x?emacs\S+ failed!'
            if 'DpkgTerminalLog' in report and re.search(regex, report['DpkgTerminalLog'], re.MULTILINE):
                for line in report['DpkgTerminalLog'].split('\n'):
                    m = re.search(r'^!! and attach the file (\S+)', line)
                    if m:
                        path = m.group(1)
                        attach_file_if_exists(report, path)
                        
        if report['Package'].startswith('linux-image-') and 'DpkgTerminalLog' in report:
            # /etc/kernel/*.d failures from kernel package postinst
            m = re.search(r'^run-parts: (/etc/kernel/\S+\.d/\S+) exited with return code \d+', report['DpkgTerminalLog'], re.MULTILINE)
            if m:
                path = m.group(1)
                package = apport.packaging.get_file_package(path)
                if package:
                    report['SourcePackage'] = package
                    report['ErrorMessage'] = m.group(0)
                else:
                    report['UnreportableReason'] = 'This failure was caused by a program which did not originate from Ubuntu'

        if 'failed to install/upgrade: corrupted filesystem tarfile' in report.get('Title', ''):
            report['UnreportableReason'] = 'This failure was caused by a corrupted package download or file system corruption.'

        if 'is already installed and configured' in report.get('ErrorMessage', ''):
            report['SourcePackage'] = 'dpkg'

    if 'Package' in report:
        package = report['Package'].split()[0]
        if package and 'attach_conffiles' in dir():
            attach_conffiles(report, package)

        # do not file bugs against "upgrade-system" if it is not installed (LP#404727)
        if package == 'upgrade-system' and 'not installed' in report['Package']:
            report['UnreportableReason'] = 'You do not have the upgrade-system package installed. Please report package upgrade failures against the package that failed to install, or against upgrade-manager.'

    # EC2 and Ubuntu Enterprise Cloud instances
    ec2_instance = False
    for pkg in ('ec2-init', 'cloud-init'):
        try:
            if apport.packaging.get_version(pkg):
                ec2_instance = True
                break
        except ValueError:
            pass
    if ec2_instance:
        metadata_url = 'http://169.254.169.254/latest/meta-data/'
        ami_id_url = urljoin(metadata_url, 'ami-id')

        try:
            ami = urlopen(ami_id_url).read()
        except:
            ami = None

        if ami is None:
            cloud = None
        elif ami.startswith('ami'):
            cloud = 'ec2'
            add_tags.append('ec2-images')
            fields = { 'Ec2AMIManifest':'ami-manifest-path',
                       'Ec2Kernel':'kernel-id',
                       'Ec2Ramdisk':'ramdisk-id',
                       'Ec2InstanceType':'instance-type',
                       'Ec2AvailabilityZone':'placement/availability-zone' }

            report['Ec2AMI'] = ami
            for key,value in fields.items():
                try:
                    report[key]=urlopen(urljoin(metadata_url, value)).read()
                except:
                    report[key]='unavailable'
        else:
            cloud = 'uec'
            add_tags.append('uec-images')

    if add_tags:
        if 'Tags' in report:
            report['Tags'] += ' ' + ' '.join(add_tags)
        else:
            report['Tags'] = ' '.join(add_tags)
