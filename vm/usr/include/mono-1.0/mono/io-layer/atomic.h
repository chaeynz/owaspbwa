/*
 * atomic.h:  Atomic operations
 *
 * Author:
 *	Dick Porter (dick@ximian.com)
 *
 * (C) 2002 Ximian, Inc.
 */

#ifndef _WAPI_ATOMIC_H_
#define _WAPI_ATOMIC_H_

#if defined(__NetBSD__)
#include <sys/param.h>

#if __NetBSD_Version__ > 499004000
#include <sys/atomic.h>
#define HAVE_ATOMIC_OPS
#endif

#endif

#include <glib.h>

#include "mono/io-layer/wapi.h"

#if defined(__NetBSD__) && defined(HAVE_ATOMIC_OPS)

#define WAPI_ATOMIC_ASM
static inline gint32 InterlockedCompareExchange(volatile gint32 *dest,
       gint32 exch, gint32 comp)
{
       return atomic_cas_32((uint32_t*)dest, comp, exch);
}

static inline gpointer InterlockedCompareExchangePointer(volatile gpointer *dest, gpointer exch, gpointer comp)
{
       return atomic_cas_ptr(dest, comp, exch);
}

static inline gint32 InterlockedIncrement(volatile gint32 *val)
{
       return atomic_inc_32_nv((uint32_t*)val);
}

static inline gint32 InterlockedDecrement(volatile gint32 *val)
{
       return atomic_dec_32_nv((uint32_t*)val);
}

static inline gint32 InterlockedExchange(volatile gint32 *val, gint32 new_val)
{
       return atomic_swap_32((uint32_t*)val, new_val);
}

static inline gpointer InterlockedExchangePointer(volatile gpointer *val,
               gpointer new_val)
{
       return atomic_swap_ptr(val, new_val);
}

static inline gint32 InterlockedExchangeAdd(volatile gint32 *val, gint32 add)
{
       return atomic_add_32_nv((uint32_t*)val, add) - add;
}

#elif defined(__i386__) || defined(__x86_64__)
#define WAPI_ATOMIC_ASM

/*
 * NB: The *Pointer() functions here assume that
 * sizeof(pointer)==sizeof(gint32)
 *
 * NB2: These asm functions assume 486+ (some of the opcodes dont
 * exist on 386).  If this becomes an issue, we can get configure to
 * fall back to the non-atomic C versions of these calls.
 */

static inline gint32 InterlockedCompareExchange(volatile gint32 *dest,
						gint32 exch, gint32 comp)
{
	gint32 old;

	__asm__ __volatile__ ("lock; cmpxchgl %2, %0"
			      : "=m" (*dest), "=a" (old)
			      : "r" (exch), "m" (*dest), "a" (comp));	
	return(old);
}

static inline gpointer InterlockedCompareExchangePointer(volatile gpointer *dest, gpointer exch, gpointer comp)
{
	gpointer old;

	__asm__ __volatile__ ("lock; "
#ifdef __x86_64__
			      "cmpxchgq"
#else
			      "cmpxchgl"
#endif
			      " %2, %0"
			      : "=m" (*dest), "=a" (old)
			      : "r" (exch), "m" (*dest), "a" (comp));	

	return(old);
}

static inline gint32 InterlockedIncrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("lock; xaddl %0, %1"
			      : "=r" (tmp), "=m" (*val)
			      : "0" (1), "m" (*val));

	return(tmp+1);
}

static inline gint32 InterlockedDecrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("lock; xaddl %0, %1"
			      : "=r" (tmp), "=m" (*val)
			      : "0" (-1), "m" (*val));

	return(tmp-1);
}

/*
 * See
 * http://msdn.microsoft.com/library/en-us/dnmag00/html/win320700.asp?frame=true
 * for the reasons for using cmpxchg and a loop here.
 *
 * That url is no longer valid, but it's still in the google cache at the
 * moment: http://www.google.com/search?q=cache:http://msdn.microsoft.com/library/en-us/dnmag00/html/win320700.asp?frame=true
 *
 * For the time being, http://msdn.microsoft.com/msdnmag/issues/0700/Win32/
 * might work.  Bet it will change soon enough though.
 */
static inline gint32 InterlockedExchange(volatile gint32 *val, gint32 new_val)
{
	gint32 ret;
	
	__asm__ __volatile__ ("1:; lock; cmpxchgl %2, %0; jne 1b"
			      : "=m" (*val), "=a" (ret)
			      : "r" (new_val), "m" (*val), "a" (*val));

	return(ret);
}

static inline gpointer InterlockedExchangePointer(volatile gpointer *val,
						  gpointer new_val)
{
	gpointer ret;
	
	__asm__ __volatile__ ("1:; lock; "
#ifdef __x86_64__
			      "cmpxchgq"
#else
			      "cmpxchgl"
#endif
			      " %2, %0; jne 1b"
			      : "=m" (*val), "=a" (ret)
			      : "r" (new_val), "m" (*val), "a" (*val));

	return(ret);
}

static inline gint32 InterlockedExchangeAdd(volatile gint32 *val, gint32 add)
{
	gint32 ret;
	
	__asm__ __volatile__ ("lock; xaddl %0, %1"
			      : "=r" (ret), "=m" (*val)
			      : "0" (add), "m" (*val));
	
	return(ret);
}

#elif (defined(sparc) || defined (__sparc__)) && defined(__GNUC__)
#define WAPI_ATOMIC_ASM

G_GNUC_UNUSED 
static inline gint32 InterlockedCompareExchange(volatile gint32 *_dest, gint32 _exch, gint32 _comp)
{
       register volatile gint32 *dest asm("g1") = _dest;
       register gint32 comp asm("o4") = _comp;
       register gint32 exch asm("o5") = _exch;

       __asm__ __volatile__(
               /* cas [%%g1], %%o4, %%o5 */
               ".word 0xdbe0500c"
               : "=r" (exch)
               : "0" (exch), "r" (dest), "r" (comp)
               : "memory");

       return exch;
}

G_GNUC_UNUSED 
static inline gpointer InterlockedCompareExchangePointer(volatile gpointer *_dest, gpointer _exch, gpointer _comp)
{
       register volatile gpointer *dest asm("g1") = _dest;
       register gpointer comp asm("o4") = _comp;
       register gpointer exch asm("o5") = _exch;

       __asm__ __volatile__(
#ifdef SPARCV9
               /* casx [%%g1], %%o4, %%o5 */
               ".word 0xdbf0500c"
#else
               /* cas [%%g1], %%o4, %%o5 */
               ".word 0xdbe0500c"
#endif
               : "=r" (exch)
               : "0" (exch), "r" (dest), "r" (comp)
               : "memory");

       return exch;
}

G_GNUC_UNUSED 
static inline gint32 InterlockedIncrement(volatile gint32 *_dest)
{
       register volatile gint32 *dest asm("g1") = _dest;
       register gint32 tmp asm("o4");
       register gint32 ret asm("o5");

       __asm__ __volatile__(
               "1:     ld      [%%g1], %%o4\n\t"
               "       add     %%o4, 1, %%o5\n\t"
               /*      cas     [%%g1], %%o4, %%o5 */
               "       .word   0xdbe0500c\n\t"
               "       cmp     %%o4, %%o5\n\t"
               "       bne     1b\n\t"
               "        add    %%o5, 1, %%o5"
               : "=&r" (tmp), "=&r" (ret)
               : "r" (dest)
               : "memory", "cc");

        return ret;
}

G_GNUC_UNUSED 
static inline gint32 InterlockedDecrement(volatile gint32 *_dest)
{
       register volatile gint32 *dest asm("g1") = _dest;
       register gint32 tmp asm("o4");
       register gint32 ret asm("o5");

       __asm__ __volatile__(
               "1:     ld      [%%g1], %%o4\n\t"
               "       sub     %%o4, 1, %%o5\n\t"
               /*      cas     [%%g1], %%o4, %%o5 */
               "       .word   0xdbe0500c\n\t"
               "       cmp     %%o4, %%o5\n\t"
               "       bne     1b\n\t"
               "        sub    %%o5, 1, %%o5"
               : "=&r" (tmp), "=&r" (ret)
               : "r" (dest)
               : "memory", "cc");

        return ret;
}

G_GNUC_UNUSED
static inline gint32 InterlockedExchange(volatile gint32 *_dest, gint32 exch)
{
       register volatile gint32 *dest asm("g1") = _dest;
       register gint32 tmp asm("o4");
       register gint32 ret asm("o5");

       __asm__ __volatile__(
               "1:     ld      [%%g1], %%o4\n\t"
               "       mov     %3, %%o5\n\t"
               /*      cas     [%%g1], %%o4, %%o5 */
               "       .word   0xdbe0500c\n\t"
               "       cmp     %%o4, %%o5\n\t"
               "       bne     1b\n\t"
               "        nop"
               : "=&r" (tmp), "=&r" (ret)
               : "r" (dest), "r" (exch)
               : "memory", "cc");

        return ret;
}

G_GNUC_UNUSED
static inline gpointer InterlockedExchangePointer(volatile gpointer *_dest, gpointer exch)
{
       register volatile gpointer *dest asm("g1") = _dest;
       register gpointer tmp asm("o4");
       register gpointer ret asm("o5");

       __asm__ __volatile__(
#ifdef SPARCV9
               "1:     ldx     [%%g1], %%o4\n\t"
#else
               "1:     ld      [%%g1], %%o4\n\t"
#endif
               "       mov     %3, %%o5\n\t"
#ifdef SPARCV9
               /*      casx    [%%g1], %%o4, %%o5 */
               "       .word   0xdbf0500c\n\t"
#else
               /*      cas     [%%g1], %%o4, %%o5 */
               "       .word   0xdbe0500c\n\t"
#endif
               "       cmp     %%o4, %%o5\n\t"
               "       bne     1b\n\t"
               "        nop"
               : "=&r" (tmp), "=&r" (ret)
               : "r" (dest), "r" (exch)
               : "memory", "cc");

        return ret;
}

G_GNUC_UNUSED
static inline gint32 InterlockedExchangeAdd(volatile gint32 *_dest, gint32 add)
{
       register volatile gint32 *dest asm("g1") = _dest;
       register gint32 tmp asm("o4");
       register gint32 ret asm("o5");

       __asm__ __volatile__(
               "1:     ld      [%%g1], %%o4\n\t"
               "       add     %%o4, %3, %%o5\n\t"
               /*      cas     [%%g1], %%o4, %%o5 */
               "       .word   0xdbe0500c\n\t"
               "       cmp     %%o4, %%o5\n\t"
               "       bne     1b\n\t"
               "        add    %%o5, %3, %%o5"
               : "=&r" (tmp), "=&r" (ret)
               : "r" (dest), "r" (add)
               : "memory", "cc");

        return ret;
}

#elif __s390__

#define WAPI_ATOMIC_ASM

static inline gint32 
InterlockedCompareExchange(volatile gint32 *dest,
			   gint32 exch, gint32 comp)
{
	gint32 old;

	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "\tLR\t%1,%3\n"
			      "\tCS\t%1,%2,0(1)\n"
			      : "+m" (*dest), "=&r" (old)
			      : "r" (exch), "r" (comp)
			      : "1", "cc");	
	return(old);
}

#ifndef __s390x__
static inline gpointer
InterlockedCompareExchangePointer(volatile gpointer *dest,
			   gpointer exch, gpointer comp)
{
	gpointer old;

	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "\tLR\t%1,%3\n"
			      "\tCS\t%1,%2,0(1)\n"
			      : "+m" (*dest), "=&r" (old)
			      : "r" (exch), "r" (comp)
			      : "1", "cc");	
	return(old);
}
# else
static inline gpointer 
InterlockedCompareExchangePointer(volatile gpointer *dest, 
				  gpointer exch, 
			          gpointer comp)
{
	gpointer old;

	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "\tLGR\t%1,%3\n"
			      "\tCSG\t%1,%2,0(1)\n"
			      : "+m" (*dest), "=&r" (old)
			      : "r" (exch), "r" (comp)
			      : "1", "cc");

	return(old);
}
# endif

# ifndef __s390x__
static inline gint32 
InterlockedIncrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tL\t%0,%1\n"
			      "\tLR\t1,%0\n"
			      "\tAHI\t1,1\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b\n"
			      "\tLR\t%0,1"
			      : "=r" (tmp), "+m" (*val)
			      : : "1", "2", "cc");

	return(tmp);
}
# else
static inline gint32 
InterlockedIncrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tLGF\t%0,%1\n"
			      "\tLGFR\t1,%0\n"
			      "\tAGHI\t1,1\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b\n"
			      "\tLGFR\t%0,1"
			      : "=r" (tmp), "+m" (*val)
			      : : "1", "2", "cc");

	return(tmp);
}
# endif

# ifndef __s390x__
static inline gint32 
InterlockedDecrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tL\t%0,%1\n"
			      "\tLR\t1,%0\n"
			      "\tAHI\t1,-1\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b\n"
			      "\tLR\t%0,1"
			      : "=r" (tmp), "+m" (*val)
			      : : "1", "2", "cc");

	return(tmp);
}
# else
static inline gint32 
InterlockedDecrement(volatile gint32 *val)
{
	gint32 tmp;
	
	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tLGF\t%0,%1\n"
			      "\tLGFR\t1,%0\n"
			      "\tAGHI\t1,-1\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b\n"
			      "\tLGFR\t%0,1"
			      : "=r" (tmp), "+m" (*val)
			      : : "1", "2", "cc");

	return(tmp);
}
# endif

static inline gint32 
InterlockedExchange(volatile gint32 *val, gint32 new_val)
{
	gint32 ret;
	
	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "0:\tL\t%1,%0\n"
			      "\tCS\t%1,%2,0(1)\n"
			      "\tJNZ\t0b"
			      : "+m" (*val), "=&r" (ret)
			      : "r" (new_val)
			      : "1", "cc");

	return(ret);
}

# ifndef __s390x__
static inline gpointer 
InterlockedExchangePointer(volatile gpointer *val, gpointer new_val)
{
	gpointer ret;
	
	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "0:\tL\t%1,%0\n"
			      "\tCS\t%1,%2,0(1)\n"
			      "\tJNZ\t0b"
			      : "+m" (*val), "=&r" (ret)
			      : "r" (new_val)
			      : "1", "cc");

	return(ret);
}
# else
static inline gpointer
InterlockedExchangePointer(volatile gpointer *val, gpointer new_val)
{
	gpointer ret;
	
	__asm__ __volatile__ ("\tLA\t1,%0\n"
			      "0:\tLG\t%1,%0\n"
			      "\tCSG\t%1,%2,0(1)\n"
			      "\tJNZ\t0b"
			      : "+m" (*val), "=&r" (ret)
			      : "r" (new_val)
			      : "1", "cc");

	return(ret);
}
# endif

# ifndef __s390x__
static inline gint32 
InterlockedExchangeAdd(volatile gint32 *val, gint32 add)
{
	gint32 ret;

	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tL\t%0,%1\n"
			      "\tLR\t1,%0\n"
			      "\tAR\t1,%2\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b"
			      : "=&r" (ret), "+m" (*val)
			      : "r" (add) 
			      : "1", "2", "cc");
	
	return(ret);
}
# else
static inline gint32 
InterlockedExchangeAdd(volatile gint32 *val, gint32 add)
{
	gint32 ret;

	__asm__ __volatile__ ("\tLA\t2,%1\n"
			      "0:\tLGF\t%0,%1\n"
			      "\tLGFR\t1,%0\n"
			      "\tAGR\t1,%2\n"
			      "\tCS\t%0,1,0(2)\n"
			      "\tJNZ\t0b"
			      : "=&r" (ret), "+m" (*val)
			      : "r" (add) 
			      : "1", "2", "cc");
	
	return(ret);
}
# endif

#elif defined(__mono_ppc__)
#define WAPI_ATOMIC_ASM

#ifdef G_COMPILER_CODEWARRIOR
static inline gint32 InterlockedIncrement(volatile register gint32 *val)
{
	gint32 result = 0, tmp;
	register gint32 result = 0;
	register gint32 tmp;

	asm
	{
		@1:
			lwarx	tmp, 0, val
			addi	result, tmp, 1
			stwcx.	result, 0, val
			bne-	@1
	}
 
	return result;
}

static inline gint32 InterlockedDecrement(register volatile gint32 *val)
{
	register gint32 result = 0;
	register gint32 tmp;

	asm
	{
		@1:
			lwarx	tmp, 0, val
			addi	result, tmp, -1
			stwcx.	result, 0, val
			bne-	@1
	}

	return result;
}
#define InterlockedCompareExchangePointer(dest,exch,comp) (void*)InterlockedCompareExchange((volatile gint32 *)(dest), (gint32)(exch), (gint32)(comp))

static inline gint32 InterlockedCompareExchange(volatile register gint32 *dest, register gint32 exch, register gint32 comp)
{
	register gint32 tmp = 0;

	asm
	{
		@1:
			lwarx	tmp, 0, dest
			cmpw	tmp, comp
			bne-	@2
			stwcx.	exch, 0, dest
			bne-	@1
		@2:
	}

	return tmp;
}
static inline gint32 InterlockedExchange(register volatile gint32 *dest, register gint32 exch)
{
	register gint32 tmp = 0;

	asm
	{
		@1:
			lwarx	tmp, 0, dest
			stwcx.	exch, 0, dest
			bne-	@1
	}

	return tmp;
}
#define InterlockedExchangePointer(dest,exch) (void*)InterlockedExchange((volatile gint32 *)(dest), (gint32)(exch))
#else

#ifdef __mono_ppc64__
#define LDREGX "ldarx"
#define STREGCXD "stdcx."
#define CMPREG "cmpd"
#else
#define LDREGX "lwarx"
#define STREGCXD "stwcx."
#define CMPREG "cmpw"
#endif

static inline gint32 InterlockedIncrement(volatile gint32 *val)
{
	gint32 result = 0, tmp;

	__asm__ __volatile__ ("\n1:\n\t"
			      "lwarx  %0, 0, %2\n\t"
			      "addi   %1, %0, 1\n\t"
                              "stwcx. %1, 0, %2\n\t"
			      "bne-   1b"
			      : "=&b" (result), "=&b" (tmp): "r" (val): "cc", "memory");
	return result + 1;
}

static inline gint32 InterlockedDecrement(volatile gint32 *val)
{
	gint32 result = 0, tmp;

	__asm__ __volatile__ ("\n1:\n\t"
			      "lwarx  %0, 0, %2\n\t"
			      "addi   %1, %0, -1\n\t"
                              "stwcx. %1, 0, %2\n\t"
			      "bne-   1b"
			      : "=&b" (result), "=&b" (tmp): "r" (val): "cc", "memory");
	return result - 1;
}

static inline gpointer InterlockedCompareExchangePointer (volatile gpointer *dest,
						gpointer exch, gpointer comp)
{
	gpointer tmp = NULL;

	__asm__ __volatile__ ("\n1:\n\t"
			     LDREGX " %0, 0, %1\n\t"
			     CMPREG " %0, %2\n\t" 
			     "bne-    2f\n\t"
			     STREGCXD " %3, 0, %1\n\t"
			     "bne-    1b\n"
			     "2:"
			     : "=&r" (tmp)
			     : "b" (dest), "r" (comp), "r" (exch): "cc", "memory");
	return(tmp);
}

static inline gint32 InterlockedCompareExchange(volatile gint32 *dest,
						gint32 exch, gint32 comp) {
	gint32 tmp = 0;

	__asm__ __volatile__ ("\n1:\n\t"
			     "lwarx   %0, 0, %1\n\t"
			     "cmpw    %0, %2\n\t" 
			     "bne-    2f\n\t"
			     "stwcx.  %3, 0, %1\n\t"
			     "bne-    1b\n"
			     "2:"
			     : "=&r" (tmp)
			     : "b" (dest), "r" (comp), "r" (exch): "cc", "memory");
	return(tmp);
}

static inline gint32 InterlockedExchange(volatile gint32 *dest, gint32 exch)
{
	gint32 tmp = 0;

	__asm__ __volatile__ ("\n1:\n\t"
			      "lwarx  %0, 0, %2\n\t"
			      "stwcx. %3, 0, %2\n\t"
			      "bne    1b"
			      : "=r" (tmp) : "0" (tmp), "b" (dest), "r" (exch): "cc", "memory");
	return(tmp);
}

static inline gpointer InterlockedExchangePointer (volatile gpointer *dest, gpointer exch)
{
	gpointer tmp = NULL;

	__asm__ __volatile__ ("\n1:\n\t"
			      LDREGX " %0, 0, %2\n\t"
			      STREGCXD " %3, 0, %2\n\t"
			      "bne    1b"
			      : "=r" (tmp) : "0" (tmp), "b" (dest), "r" (exch): "cc", "memory");
	return(tmp);
}

static inline gint32 InterlockedExchangeAdd(volatile gint32 *dest, gint32 add)
{
        gint32 result, tmp;
        __asm__ __volatile__ ("\n1:\n\t"
                              "lwarx  %0, 0, %2\n\t"
                              "add    %1, %0, %3\n\t"
                              "stwcx. %1, 0, %2\n\t"
                              "bne    1b"
                              : "=&r" (result), "=&r" (tmp)
                              : "r" (dest), "r" (add) : "cc", "memory");
        return(result);
}

#undef LDREGX
#undef STREGCXD
#undef CMPREG

#endif /* !G_COMPILER_CODEWARRIOR */

#elif defined(__arm__)
#define WAPI_ATOMIC_ASM

static inline gint32 InterlockedCompareExchange(volatile gint32 *dest, gint32 exch, gint32 comp)
{
#ifdef __thumb__
	return __sync_val_compare_and_swap (dest, comp, exch);
#else
	int a, b;

	__asm__ __volatile__ (    "0:\n\t"
				  "ldr %1, [%2]\n\t"
				  "cmp %1, %4\n\t"
				  "mov %0, %1\n\t"
				  "bne 1f\n\t"
				  "swp %0, %3, [%2]\n\t"
				  "cmp %0, %1\n\t"
				  "swpne %3, %0, [%2]\n\t"
				  "bne 0b\n\t"
				  "1:"
				  : "=&r" (a), "=&r" (b)
				  : "r" (dest), "r" (exch), "r" (comp)
				  : "cc", "memory");

	return a;
#endif /* !__thumb__ */
}

static inline gpointer InterlockedCompareExchangePointer(volatile gpointer *dest, gpointer exch, gpointer comp)
{
#ifdef __thumb__
	return __sync_val_compare_and_swap (dest, comp, exch);
#else
	gpointer a, b;

	__asm__ __volatile__ (    "0:\n\t"
				  "ldr %1, [%2]\n\t"
				  "cmp %1, %4\n\t"
				  "mov %0, %1\n\t"
				  "bne 1f\n\t"
				  "swpeq %0, %3, [%2]\n\t"
				  "cmp %0, %1\n\t"
				  "swpne %3, %0, [%2]\n\t"
				  "bne 0b\n\t"
				  "1:"
				  : "=&r" (a), "=&r" (b)
				  : "r" (dest), "r" (exch), "r" (comp)
				  : "cc", "memory");

	return a;
#endif
}

static inline gint32 InterlockedIncrement(volatile gint32 *dest)
{
#ifdef __thumb__
	return __sync_add_and_fetch (dest, 1);
#else
	int a, b, c;

	__asm__ __volatile__ (  "0:\n\t"
				"ldr %0, [%3]\n\t"
				"add %1, %0, %4\n\t"
				"swp %2, %1, [%3]\n\t"
				"cmp %0, %2\n\t"
				"swpne %1, %2, [%3]\n\t"
				"bne 0b"
				: "=&r" (a), "=&r" (b), "=&r" (c)
				: "r" (dest), "r" (1)
				: "cc", "memory");

	return b;
#endif
}

static inline gint32 InterlockedDecrement(volatile gint32 *dest)
{
#ifdef __thumb__
	return __sync_sub_and_fetch (dest, 1);
#else
	int a, b, c;

	__asm__ __volatile__ (  "0:\n\t"
				"ldr %0, [%3]\n\t"
				"add %1, %0, %4\n\t"
				"swp %2, %1, [%3]\n\t"
				"cmp %0, %2\n\t"
				"swpne %1, %2, [%3]\n\t"
				"bne 0b"
				: "=&r" (a), "=&r" (b), "=&r" (c)
				: "r" (dest), "r" (-1)
				: "cc", "memory");

	return b;
#endif
}

static inline gint32 InterlockedExchange(volatile gint32 *dest, gint32 exch)
{
#ifdef __thumb__
	return __sync_lock_test_and_set (dest, exch);
#else
	int a;

	__asm__ __volatile__ (  "swp %0, %2, [%1]"
				: "=&r" (a)
				: "r" (dest), "r" (exch));

	return a;
#endif
}

static inline gpointer InterlockedExchangePointer(volatile gpointer *dest, gpointer exch)
{
#ifdef __thumb__
	return __sync_lock_test_and_set (dest, exch);
#else
	gpointer a;

	__asm__ __volatile__ (	"swp %0, %2, [%1]"
				: "=&r" (a)
				: "r" (dest), "r" (exch));

	return a;
#endif
}

static inline gint32 InterlockedExchangeAdd(volatile gint32 *dest, gint32 add)
{
#ifdef __thumb__
	return __sync_fetch_and_add (dest, add);
#else
	int a, b, c;

	__asm__ __volatile__ (  "0:\n\t"
				"ldr %0, [%3]\n\t"
				"add %1, %0, %4\n\t"
				"swp %2, %1, [%3]\n\t"
				"cmp %0, %2\n\t"
				"swpne %1, %2, [%3]\n\t"
				"bne 0b"
				: "=&r" (a), "=&r" (b), "=&r" (c)
				: "r" (dest), "r" (add)
				: "cc", "memory");

	return a;
#endif
}

#elif defined(__ia64__)
#define WAPI_ATOMIC_ASM

#ifdef __INTEL_COMPILER
#include <ia64intrin.h>
#endif

static inline gint32 InterlockedCompareExchange(gint32 volatile *dest,
						gint32 exch, gint32 comp)
{
	gint32 old;
	guint64 real_comp;

#ifdef __INTEL_COMPILER
	old = _InterlockedCompareExchange (dest, exch, comp);
#else
	/* cmpxchg4 zero extends the value read from memory */
	real_comp = (guint64)(guint32)comp;
	asm volatile ("mov ar.ccv = %2 ;;\n\t"
				  "cmpxchg4.acq %0 = [%1], %3, ar.ccv\n\t"
				  : "=r" (old) : "r" (dest), "r" (real_comp), "r" (exch));
#endif

	return(old);
}

static inline gpointer InterlockedCompareExchangePointer(gpointer volatile *dest,
						gpointer exch, gpointer comp)
{
	gpointer old;

#ifdef __INTEL_COMPILER
	old = _InterlockedCompareExchangePointer (dest, exch, comp);
#else
	asm volatile ("mov ar.ccv = %2 ;;\n\t"
				  "cmpxchg8.acq %0 = [%1], %3, ar.ccv\n\t"
				  : "=r" (old) : "r" (dest), "r" (comp), "r" (exch));
#endif

	return(old);
}

static inline gint32 InterlockedIncrement(gint32 volatile *val)
{
#ifdef __INTEL_COMPILER
	return _InterlockedIncrement (val);
#else
	gint32 old;

	do {
		old = *val;
	} while (InterlockedCompareExchange (val, old + 1, old) != old);

	return old + 1;
#endif
}

static inline gint32 InterlockedDecrement(gint32 volatile *val)
{
#ifdef __INTEL_COMPILER
	return _InterlockedDecrement (val);
#else
	gint32 old;

	do {
		old = *val;
	} while (InterlockedCompareExchange (val, old - 1, old) != old);

	return old - 1;
#endif
}

static inline gint32 InterlockedExchange(gint32 volatile *dest, gint32 new_val)
{
#ifdef __INTEL_COMPILER
	return _InterlockedExchange (dest, new_val);
#else
	gint32 res;

	do {
		res = *dest;
	} while (InterlockedCompareExchange (dest, new_val, res) != res);

	return res;
#endif
}

static inline gpointer InterlockedExchangePointer(gpointer volatile *dest, gpointer new_val)
{
#ifdef __INTEL_COMPILER
	return (gpointer)_InterlockedExchange64 ((gint64*)dest, (gint64)new_val);
#else
	gpointer res;

	do {
		res = *dest;
	} while (InterlockedCompareExchangePointer (dest, new_val, res) != res);

	return res;
#endif
}

static inline gint32 InterlockedExchangeAdd(gint32 volatile *val, gint32 add)
{
	gint32 old;

#ifdef __INTEL_COMPILER
	old = _InterlockedExchangeAdd (val, add);
#else
	do {
		old = *val;
	} while (InterlockedCompareExchange (val, old + add, old) != old);

	return old;
#endif
}

#elif defined(__alpha__)
#define WAPI_ATOMIC_ASM

static inline gint32 InterlockedCompareExchange(volatile gint32 *dest,
						gint32 exch, gint32 comp)
{
	gint32 old, temp, temp2;
	long compq = comp, exchq = exch;

	__asm__ __volatile__ (
		"1:	ldl_l %2, %0\n"
		"	mov %2, %1\n"
		"	cmpeq %2, %5, %3\n"
		"	cmovne %3, %4, %2\n"
		"	stl_c %2, %0\n"
		"	beq %2, 1b\n"
		: "=m" (*dest), "=&r" (old), "=&r" (temp), "=&r" (temp2)
		: "r" (exchq), "r" (compq), "m" (*dest));
	return(old);
}

static inline gpointer InterlockedCompareExchangePointer(volatile gpointer *dest, gpointer exch, gpointer comp)
{
	gpointer old, temp, temp2;

	__asm__ __volatile__ (
		"1:	ldq_l %2, %0\n"
		"	mov %2, %1\n"
		"	cmpeq %2, %5, %3\n"
		"	cmovne %3, %4, %2\n"
		"	stq_c %2, %0\n"
		"	beq %2, 1b\n"
		: "=m" (*dest), "=&r" (old), "=&r" (temp), "=&r" (temp2)
		: "r" (exch), "r" (comp), "m" (*dest));
	return(old);
}

static inline gint32 InterlockedIncrement(volatile gint32 *val)
{
	gint32 temp, cur;
	
	__asm__ __volatile__ (
		"1:	ldl_l %0, %1\n"
		"	addl %0, %3, %0\n"
		"	mov %0, %2\n"
		"	stl_c %0, %1\n"
		"	beq %0, 1b\n"
		: "=&r" (temp), "=m" (*val), "=r" (cur)
		: "Ir" (1), "m" (*val));
	return(cur);
}

static inline gint32 InterlockedDecrement(volatile gint32 *val)
{
	gint32 temp, cur;
	
	__asm__ __volatile__ (
		"1:	ldl_l %0, %1\n"
		"	subl %0, %3, %0\n"
		"	mov %0, %2\n"
		"	stl_c %0, %1\n"
		"	beq %0, 1b\n"
		: "=&r" (temp), "=m" (*val), "=r" (cur)
		: "Ir" (1), "m" (*val));
	return(cur);
}

static inline gint32 InterlockedExchange(volatile gint32 *val, gint32 new_val)
{
	gint32 ret, temp;

	__asm__ __volatile__ (
		"1:	ldl_l %1, %0\n"
		"	mov %3, %2\n"
		"	stl_c %2, %0\n"
		"	beq %2, 1b\n"
		: "=m" (*val), "=&r" (ret), "=&r" (temp)
		: "r" (new_val), "m" (*val));
	return(ret);
}

static inline gpointer InterlockedExchangePointer(volatile gpointer *val, gpointer new_val)
{
	gpointer ret, temp;

	__asm__ __volatile__ (
		"1:	ldq_l %1, %0\n"
		"	mov %3, %2\n"
		"	stq_c %2, %0\n"
		"	beq %2, 1b\n"
		: "=m" (*val), "=&r" (ret), "=&r" (temp)
		: "r" (new_val), "m" (*val));
	return(ret);
}

static inline gint32 InterlockedExchangeAdd(volatile gint32 *val, gint32 add)
{
	gint32 ret, temp;
	
	__asm__ __volatile__ (
		"1:	ldl_l	%2, %0\n"
		"	mov	%2, %1\n"
		"	addl	%2, %3, %2\n"
		"	stl_c	%2, %0\n"
		"	beq	%2, 1b\n"
		: "=m" (*val), "=&r" (ret), "=&r" (temp)
		: "r" (add), "m" (*val));
	
	return(ret);
}

#elif defined(__mips__)
#define WAPI_ATOMIC_ASM

static inline gint32 InterlockedIncrement(volatile gint32 *val)
{
	gint32 tmp, result = 0;

	__asm__ __volatile__ ("    .set    mips32\n"
			      "1:  ll      %0, %2\n"
			      "    addu    %1, %0, 1\n"
                              "    sc      %1, %2\n"
			      "    beqz    %1, 1b\n"
			      "    .set    mips0\n"
			      : "=&r" (result), "=&r" (tmp), "=m" (*val)
			      : "m" (*val));
	return result + 1;
}

static inline gint32 InterlockedDecrement(volatile gint32 *val)
{
	gint32 tmp, result = 0;

	__asm__ __volatile__ ("    .set    mips32\n"
			      "1:  ll      %0, %2\n"
			      "    subu    %1, %0, 1\n"
                              "    sc      %1, %2\n"
			      "    beqz    %1, 1b\n"
			      "    .set    mips0\n"
			      : "=&r" (result), "=&r" (tmp), "=m" (*val)
			      : "m" (*val));
	return result - 1;
}

#define InterlockedCompareExchangePointer(dest,exch,comp) InterlockedCompareExchange((volatile gint32 *)(dest), (gint32)(exch), (gint32)(comp))

static inline gint32 InterlockedCompareExchange(volatile gint32 *dest,
						gint32 exch, gint32 comp) {
	gint32 old, tmp;

	__asm__ __volatile__ ("    .set    mips32\n"
			      "1:  ll      %0, %2\n"
			      "    bne     %0, %5, 2f\n"
			      "    move    %1, %4\n"
                              "    sc      %1, %2\n"
			      "    beqz    %1, 1b\n"
			      "2:  .set    mips0\n"
			      : "=&r" (old), "=&r" (tmp), "=m" (*dest)
			      : "m" (*dest), "r" (exch), "r" (comp));
	return(old);
}

static inline gint32 InterlockedExchange(volatile gint32 *dest, gint32 exch)
{
	gint32 result, tmp;

	__asm__ __volatile__ ("    .set    mips32\n"
			      "1:  ll      %0, %2\n"
			      "    move    %1, %4\n"
                              "    sc      %1, %2\n"
			      "    beqz    %1, 1b\n"
			      "    .set    mips0\n"
			      : "=&r" (result), "=&r" (tmp), "=m" (*dest)
			      : "m" (*dest), "r" (exch));
	return(result);
}
#define InterlockedExchangePointer(dest,exch) InterlockedExchange((volatile gint32 *)(dest), (gint32)(exch))

static inline gint32 InterlockedExchangeAdd(volatile gint32 *dest, gint32 add)
{
        gint32 result, tmp;

	__asm__ __volatile__ ("    .set    mips32\n"
			      "1:  ll      %0, %2\n"
			      "    addu    %1, %0, %4\n"
                              "    sc      %1, %2\n"
			      "    beqz    %1, 1b\n"
			      "    .set    mips0\n"
			      : "=&r" (result), "=&r" (tmp), "=m" (*dest)
			      : "m" (*dest), "r" (add));
        return result;
}

#else

extern gint32 InterlockedCompareExchange(volatile gint32 *dest, gint32 exch, gint32 comp);
extern gpointer InterlockedCompareExchangePointer(volatile gpointer *dest, gpointer exch, gpointer comp);
extern gint32 InterlockedIncrement(volatile gint32 *dest);
extern gint32 InterlockedDecrement(volatile gint32 *dest);
extern gint32 InterlockedExchange(volatile gint32 *dest, gint32 exch);
extern gpointer InterlockedExchangePointer(volatile gpointer *dest, gpointer exch);
extern gint32 InterlockedExchangeAdd(volatile gint32 *dest, gint32 add);

#if defined(__hppa__)
#define WAPI_ATOMIC_ASM
#endif

#endif

#endif /* _WAPI_ATOMIC_H_ */
