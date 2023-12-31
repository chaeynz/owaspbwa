/* @(#)types.h	2.3 88/08/15 4.0 RPCSRC */
/*
 * Sun RPC is a product of Sun Microsystems, Inc. and is provided for
 * unrestricted use provided that this legend is included on all tape
 * media and as a part of the software program in whole or part.  Users
 * may copy or modify Sun RPC without charge, but are not authorized
 * to license or distribute it to anyone else except as part of a product or
 * program developed by the user.
 *
 * SUN RPC IS PROVIDED AS IS WITH NO WARRANTIES OF ANY KIND INCLUDING THE
 * WARRANTIES OF DESIGN, MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE OR TRADE PRACTICE.
 *
 * Sun RPC is provided with no support and without any obligation on the
 * part of Sun Microsystems, Inc. to assist in its use, correction,
 * modification or enhancement.
 *
 * SUN MICROSYSTEMS, INC. SHALL HAVE NO LIABILITY WITH RESPECT TO THE
 * INFRINGEMENT OF COPYRIGHTS, TRADE SECRETS OR ANY PATENTS BY SUN RPC
 * OR ANY PART THEREOF.
 *
 * In no event will Sun Microsystems, Inc. be liable for any lost revenue
 * or profits or other special, indirect and consequential damages, even if
 * Sun has been advised of the possibility of such damages.
 *
 * Sun Microsystems, Inc.
 * 2550 Garcia Avenue
 * Mountain View, California  94043
 */
/*      @(#)types.h 1.18 87/07/24 SMI      */

/*
 * Rpc additions to <sys/types.h>
 */
#ifndef GSSRPC_TYPES_H
#define GSSRPC_TYPES_H

#include <sys/types.h>

#include <sys/select.h>
#include <sys/time.h>
#include <unistd.h>

/*
 * Try to get MAXHOSTNAMELEN from somewhere.
 */
#include <sys/param.h>
/* #include <netdb.h> */

/* Get htonl(), ntohl(), etc. */
#include <netinet/in.h>

#include <stdlib.h>

/*
 * Pull in typedefs for fixed-width integers somehow, if they weren't
 * in sys/types.h.
 */
#include <stdint.h>
/* #include <inttypes.h> */
/* Define if there is no uint32_t in system headers. */
/* #undef GSSRPC__FAKE_INT32 */

#include <limits.h>

#ifndef GSSRPC__BEGIN_DECLS
#ifdef __cplusplus
#define GSSRPC__BEGIN_DECLS	extern "C" {
#define GSSRPC__END_DECLS	}
#else
#define GSSRPC__BEGIN_DECLS
#define GSSRPC__END_DECLS
#endif
#endif

GSSRPC__BEGIN_DECLS

#if defined(CHAR_BIT) && CHAR_BIT != 8
#error "Bytes must be exactly 8 bits."
#endif

/*
 * If no uint32_t in system headers, fake it by looking for a 32-bit
 * two's-complement type.  Yes, this stomps on POSIX namespace, but if
 * we get here, we're on a system that's far from being
 * POSIX-compliant anyway.
 */
#if GSSRPC__FAKE_UINT32
#if (UINT_MAX == 0xffffffffUL) && (INT_MAX == 0x7fffffffL) \
	&& (INT_MIN == -INT_MAX-1)
typedef int		int32_t;
typedef unsigned int	uint32_t;
#else
#if (ULONG_MAX == 0xffffffffUL) && (LONG_MAX == 0x7fffffffL) \
	&& (LONG_MIN == -LONG_MAX-1)
typedef long		int32_t;
typedef unsigned long	uint32_t;
#else
#if (USHRT_MAX == 0xffffffffUL) && (SHRT_MAX == 0x7fffffffL) \
	&& (SHRT_MIN == -SHRT_MAX-1)
typedef short		int32_t;
typedef unsigned short	uint32_t;
#else
#error "Can't fake up uint32_t."
#endif
#endif
#endif
#endif /* GSSRPC__FAKE_UINT32 */

#if (LONG_MIN != -LONG_MAX-1) || (INT_MIN != -INT_MAX-1) \
	|| (SHRT_MIN != -SHRT_MAX-1)
#error "Integer types must be two's-complement."
#endif

/* Define if we need to fake up some BSD type aliases. */
#ifndef GSSRPC__BSD_TYPEALIASES	/* Allow application to override. */
/* #undef GSSRPC__BSD_TYPEALIASES */
#endif
#if GSSRPC__BSD_TYPEALIASES
typedef unsigned char	u_char;
typedef unsigned short	u_short;
typedef unsigned int	u_int;
typedef unsigned long	u_long;
#endif

typedef uint32_t	rpcprog_t;
typedef uint32_t	rpcvers_t;
typedef uint32_t	rpcprot_t;
typedef uint32_t	rpcproc_t;
typedef uint32_t	rpcport_t;
typedef int32_t		rpc_inline_t;

/* This is for rpc/netdb.h */
#define STRUCT_RPCENT_IN_RPC_NETDB_H

#define	bool_t	int
#define	enum_t	int
#ifndef FALSE
#	define	FALSE	(0)
#endif
#ifndef TRUE
#	define	TRUE	(1)
#endif
/* XXX namespace */
#define __dontcare__	-1
#ifndef NULL
#	define NULL 0
#endif

/*
 * The below should probably be internal-only, but seem to be
 * traditionally exported in RPC implementations.
 */
#define mem_alloc(bsize)	malloc(bsize)
#define mem_free(ptr, bsize)	free(ptr)

#if 0
#include <netdb.h> /* XXX This should not have to be here.
		    * I got sick of seeing the warnings for MAXHOSTNAMELEN
		    * and the two values were different. -- shanzer
		    */
#endif

#ifndef INADDR_LOOPBACK
#define       INADDR_LOOPBACK         (uint32_t)0x7F000001
#endif
#ifndef MAXHOSTNAMELEN
#define        MAXHOSTNAMELEN  64
#endif

GSSRPC__END_DECLS

#include <gssrpc/rename.h>

#endif /* !defined(GSSRPC_TYPES_H) */
