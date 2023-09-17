# Copyright 2009 Canonical Ltd.  All rights reserved.
#
# This file is part of lazr.restfulclient
#
# lazr.restfulclient is free software: you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, version 3 of the License.
#
# lazr.restfulclient is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
# License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with lazr.restfulclient.  If not, see <http://www.gnu.org/licenses/>.
"Test harness for doctests."

# pylint: disable-msg=E0611,W0142

__metaclass__ = type
__all__ = [
    'additional_tests',
    ]

import atexit
import doctest
import os
from pkg_resources import (
    resource_filename, resource_exists, resource_listdir, cleanup_resources)
import unittest
import wsgi_intercept
from wsgi_intercept.httplib2_intercept import install, uninstall

from zope.component import getUtility

from lazr.restful.example.base.interfaces import IFileManager
from lazr.restful.example.base.tests.test_integration import WSGILayer
from lazr.restful.testing.webservice import WebServiceApplication


DOCTEST_FLAGS = (
    doctest.ELLIPSIS |
    doctest.NORMALIZE_WHITESPACE |
    doctest.REPORT_NDIFF)


def setUp(test):
    install()
    wsgi_intercept.add_wsgi_intercept(
        'cookbooks.dev', 80, WSGILayer.make_application)


def tearDown(test):
    uninstall()
    file_manager = getUtility(IFileManager)
    file_manager.files = {}
    file_manager.counter = 0


def additional_tests():
    "Run the doc tests (README.txt and docs/*, if any exist)"
    doctest_files = [
        os.path.abspath(resource_filename('lazr.restfulclient', 'README.txt'))]
    if resource_exists('lazr.restfulclient', 'docs'):
        for name in resource_listdir('lazr.restfulclient', 'docs'):
            if name.endswith('.txt'):
                doctest_files.append(
                    os.path.abspath(
                        resource_filename('lazr.restfulclient', 'docs/%s' % name)))
    kwargs = dict(module_relative=False, optionflags=DOCTEST_FLAGS,
                  setUp=setUp, tearDown=tearDown)
    atexit.register(cleanup_resources)
    suite = doctest.DocFileSuite(*doctest_files, **kwargs)
    suite.layer = WSGILayer
    return suite
