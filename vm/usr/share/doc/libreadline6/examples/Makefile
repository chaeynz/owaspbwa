#
# This is the Makefile for the readline examples subdirectory.
#
# Copyright (C) 1994,2008,2009 Free Software Foundation, Inc.

#   This program is free software: you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.

#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.

#   You should have received a copy of the GNU General Public License
#   along with this program.  If not, see <http://www.gnu.org/licenses/>.

RL_LIBRARY_VERSION = 6.1

SHELL = /bin/sh
RM = rm -f

prefix = /usr
exec_prefix = ${prefix}

datarootdir = ${prefix}/share

bindir = ${exec_prefix}/bin
srcdir = /build/buildd/readline6-6.1/examples
datadir = ${datarootdir}
VPATH = .:/build/buildd/readline6-6.1/examples
top_srcdir = /build/buildd/readline6-6.1
#BUILD_DIR = .
BUILD_DIR = /build/buildd/readline6-6.1/build
installdir = $(datadir)/readline

INSTALL = /usr/bin/install -c
INSTALL_PROGRAM = ${INSTALL}
INSTALL_DATA = ${INSTALL} -m 644

EXEEXT = 
OBJEXT = o

# Support an alternate destination root directory for package building
DESTDIR =

DEFS = -DHAVE_CONFIG_H
CC = gcc
CFLAGS = -g -O2
LOCAL_CFLAGS =  -DREADLINE_LIBRARY -DRL_LIBRARY_VERSION='"$(RL_LIBRARY_VERSION)"'
CPPFLAGS = 

INCLUDES = -I$(srcdir) -I$(top_srcdir) -I..

CCFLAGS  = $(DEFS) $(LOCAL_CFLAGS) $(CPPFLAGS) $(INCLUDES) $(CFLAGS)
LDFLAGS = -g -L.. -Wl,-Bsymbolic-functions

PURIFY = 

READLINE_LIB = ../libreadline.a
HISTORY_LIB = ../libhistory.a

TERMCAP_LIB = -lcurses

.c.o:
	${RM} $@
	$(CC) $(CCFLAGS) -c $<

SOURCES = excallback.c fileman.c histexamp.c manexamp.c rl-fgets.c rl.c \
		rlcat.c rlevent.c rlptytest.c rltest.c rlversion.c

EXECUTABLES = fileman$(EXE) rltest$(EXE) rl$(EXE) rlcat$(EXE) rlevent$(EXE) \
		rlversion$(EXE) histexamp$(EXE)
OBJECTS = fileman.o rltest.o rl.o rlevent.o rlcat.o rlversion.o histexamp.o

all: $(EXECUTABLES)
everything: all

check:	rlversion$(EXE)
	@echo Readline version: `rlversion$(EXE)`

installdirs:
	-$(SHELL) $(top_srcdir)/support/mkdirs $(DESTDIR)$(installdir)

install:	installdirs
	@for f in $(SOURCES); do \
		$(RM) $(DESTDIR)$(installdir)/$$f ; \
		$(INSTALL_DATA) $$f $(DESTDIR)$(installdir) ; \
	done
	
uninstall:
	@for f in $(SOURCES); do \
		$(RM) $(DESTDIR)$(installdir)/$$f ; \
	done
	-rmdir $(DESTDIR)$(installdir)
	
rl$(EXE): rl.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ rl.o $(READLINE_LIB) $(TERMCAP_LIB)

rlcat$(EXE): rlcat.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ rlcat.o $(READLINE_LIB) $(TERMCAP_LIB)

rlevent$(EXE): rlevent.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ rlevent.o $(READLINE_LIB) $(TERMCAP_LIB)

fileman$(EXE): fileman.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ fileman.o $(READLINE_LIB) $(TERMCAP_LIB)

rltest$(EXE): rltest.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ rltest.o $(READLINE_LIB) $(TERMCAP_LIB)

rlptytest$(EXE): rlptytest.o $(READLINE_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ rlptytest.o $(READLINE_LIB) $(TERMCAP_LIB)

rlversion$(EXE): rlversion.o $(READLINE_LIB)
	$(CC) $(LDFLAGS) -o $@ rlversion.o $(READLINE_LIB) $(TERMCAP_LIB)

histexamp$(EXE): histexamp.o $(HISTORY_LIB)
	$(PURIFY) $(CC) $(LDFLAGS) -o $@ histexamp.o -lhistory $(TERMCAP_LIB)

clean mostlyclean:
	$(RM) $(OBJECTS)
	$(RM) $(EXECUTABLES) *.exe

distclean maintainer-clean: clean
	$(RM) Makefile

fileman.o: fileman.c
rltest.o: rltest.c
rl.o: rl.c
rlversion.o: rlversion.c
histexamp.o: histexamp.c
rlcat.o: rlcat.c
rlptytest.o: rlptytest.c

fileman.o: $(top_srcdir)/readline.h
rltest.o: $(top_srcdir)/readline.h
rl.o: $(top_srcdir)/readline.h
rlversion.o: $(top_srcdir)/readline.h
histexamp.o: $(top_srcdir)/history.h
rlcat.o: $(top_srcdir)/readline.h $(top_srcdir)/history.h
rlptytest.o: $(top_srcdir)/readline.h $(top_srcdir)/history.h
