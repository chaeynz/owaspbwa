/* **********************************************************
 * Copyright 2005-2008 VMware, Inc.  All rights reserved. -- VMware Confidential
 * **********************************************************/

/*
 * vmGuestlibTest.c
 *
 * Sample/test code for the VMware Guest API.
 *
 * This can be compiled for Linux by doing something like the following:
 *
 * gcc -g -o vmguestlibtest -ldl -I<path to VMware headers> vmGuestLibTest.c
 *
 */


#include <stdlib.h>
#include <stdarg.h>
#include <stdio.h>
#ifdef _WIN32
#   include <windows.h>
#else
#   include <unistd.h>
#   include <dlfcn.h>
#endif

#include "vmGuestLib.h"



#ifdef _WIN32
#define SLEEP(x) Sleep(x * 1000)
#else
#define SLEEP(x) sleep(x)
#endif


static Bool done = FALSE;


/* Functions to dynamically load from the GuestLib library. */
char const * (*GuestLib_GetErrorText)(VMGuestLibError);
VMGuestLibError (*GuestLib_OpenHandle)(VMGuestLibHandle*);
VMGuestLibError (*GuestLib_CloseHandle)(VMGuestLibHandle);
VMGuestLibError (*GuestLib_UpdateInfo)(VMGuestLibHandle handle);
VMGuestLibError (*GuestLib_GetSessionId)(VMGuestLibHandle handle,
                                         VMSessionId *id);
VMGuestLibError (*GuestLib_GetCpuReservationMHz)(VMGuestLibHandle handle,
                                                 uint32 *cpuReservationMHz);
VMGuestLibError (*GuestLib_GetCpuLimitMHz)(VMGuestLibHandle handle, uint32 *cpuLimitMHz);
VMGuestLibError (*GuestLib_GetCpuShares)(VMGuestLibHandle handle, uint32 *cpuShares);
VMGuestLibError (*GuestLib_GetCpuUsedMs)(VMGuestLibHandle handle, uint64 *cpuUsedMs);
VMGuestLibError (*GuestLib_GetHostProcessorSpeed)(VMGuestLibHandle handle, uint32 *mhz);
VMGuestLibError (*GuestLib_GetMemReservationMB)(VMGuestLibHandle handle,
                                                uint32 *memReservationMB);
VMGuestLibError (*GuestLib_GetMemLimitMB)(VMGuestLibHandle handle, uint32 *memLimitMB);
VMGuestLibError (*GuestLib_GetMemShares)(VMGuestLibHandle handle, uint32 *memShares);
VMGuestLibError (*GuestLib_GetMemMappedMB)(VMGuestLibHandle handle,
                                           uint32 *memMappedMB);
VMGuestLibError (*GuestLib_GetMemActiveMB)(VMGuestLibHandle handle, uint32 *memActiveMB);
VMGuestLibError (*GuestLib_GetMemOverheadMB)(VMGuestLibHandle handle,
                                             uint32 *memOverheadMB);
VMGuestLibError (*GuestLib_GetMemBalloonedMB)(VMGuestLibHandle handle,
                                              uint32 *memBalloonedMB);
VMGuestLibError (*GuestLib_GetMemSwappedMB)(VMGuestLibHandle handle,
                                            uint32 *memSwappedMB);
VMGuestLibError (*GuestLib_GetMemSharedMB)(VMGuestLibHandle handle,
                                           uint32 *memSharedMB);
VMGuestLibError (*GuestLib_GetMemSharedSavedMB)(VMGuestLibHandle handle,
                                                uint32 *memSharedSavedMB);
VMGuestLibError (*GuestLib_GetMemUsedMB)(VMGuestLibHandle handle,
                                         uint32 *memUsedMB);
VMGuestLibError (*GuestLib_GetElapsedMs)(VMGuestLibHandle handle, uint64 *elapsedMs);
VMGuestLibError (*GuestLib_GetResourcePoolPath)(VMGuestLibHandle handle,
                                                size_t *bufferSize,
                                                char *pathBuffer);

/*
 * Handle for use with shared library.
 */

#ifdef _WIN32
HMODULE dlHandle = NULL;
#else
void *dlHandle = NULL;
#endif

/*
 * GuestLib handle.
 */
VMGuestLibHandle glHandle;



/*
 * Macro to load a single GuestLib function from the shared library.
 */

#ifdef _WIN32
#define LOAD_ONE_FUNC(funcname)                                      \
   do {                                                              \
      (FARPROC)funcname = GetProcAddress(dlHandle, "VM" #funcname);  \
      if (funcname == NULL) {                                        \
         error = GetLastError();                                     \
         printf("Failed to load \'%s\': %d\n",                       \
                #funcname, error);                                   \
         return FALSE;                                               \
      }                                                              \
   } while (0)

#else

#define LOAD_ONE_FUNC(funcname)                           \
   do {                                                   \
      funcname = dlsym(dlHandle, "VM" #funcname);         \
      if ((dlErrStr = dlerror()) != NULL) {               \
         printf("Failed to load \'%s\': \'%s\'\n",        \
                #funcname, dlErrStr);                     \
         return FALSE;                                    \
      }                                                   \
   } while (0)

#endif


/*
 *-----------------------------------------------------------------------------
 *
 * LoadFunctions --
 *
 *      Load the functions from the shared library.
 *
 * Results:
 *      TRUE on success
 *      FALSE on failure
 *
 * Side effects:
 *      None
 *
 *-----------------------------------------------------------------------------
 */

Bool
LoadFunctions(void)
{
   /*
    * First, try to load the shared library.
    */
#ifdef _WIN32
   DWORD error;

   dlHandle = LoadLibrary("vmGuestLib.dll");
   if (!dlHandle) {
      error = GetLastError();
      printf("LoadLibrary failed: %d\n", error);
      return FALSE;
   }
#else
   char const *dlErrStr;

   dlHandle = dlopen("libvmGuestLib.so", RTLD_NOW);
   if (!dlHandle) {
      dlErrStr = dlerror();
      printf("dlopen failed: \'%s\'\n", dlErrStr);
      return FALSE;
   }
#endif

   /* Load all the individual library functions. */
   LOAD_ONE_FUNC(GuestLib_GetErrorText);
   LOAD_ONE_FUNC(GuestLib_OpenHandle);
   LOAD_ONE_FUNC(GuestLib_CloseHandle);
   LOAD_ONE_FUNC(GuestLib_UpdateInfo);
   LOAD_ONE_FUNC(GuestLib_GetSessionId);
   LOAD_ONE_FUNC(GuestLib_GetCpuReservationMHz);
   LOAD_ONE_FUNC(GuestLib_GetCpuLimitMHz);
   LOAD_ONE_FUNC(GuestLib_GetCpuShares);
   LOAD_ONE_FUNC(GuestLib_GetCpuUsedMs);
   LOAD_ONE_FUNC(GuestLib_GetHostProcessorSpeed);
   LOAD_ONE_FUNC(GuestLib_GetMemReservationMB);
   LOAD_ONE_FUNC(GuestLib_GetMemLimitMB);
   LOAD_ONE_FUNC(GuestLib_GetMemShares);
   LOAD_ONE_FUNC(GuestLib_GetMemMappedMB);
   LOAD_ONE_FUNC(GuestLib_GetMemActiveMB);
   LOAD_ONE_FUNC(GuestLib_GetMemOverheadMB);
   LOAD_ONE_FUNC(GuestLib_GetMemBalloonedMB);
   LOAD_ONE_FUNC(GuestLib_GetMemSwappedMB);
   LOAD_ONE_FUNC(GuestLib_GetMemSharedMB);
   LOAD_ONE_FUNC(GuestLib_GetMemSharedSavedMB);
   LOAD_ONE_FUNC(GuestLib_GetMemUsedMB);
   LOAD_ONE_FUNC(GuestLib_GetElapsedMs);
   LOAD_ONE_FUNC(GuestLib_GetResourcePoolPath);

   return TRUE;
}


/*
 *-----------------------------------------------------------------------------
 *
 * TestGuestLib --
 *
 *      Test the VMware Guest API.
 *
 * Results:
 *      TRUE on success
 *      FALSE on failure
 *
 * Side effects:
 *      None
 *
 *-----------------------------------------------------------------------------
 */

Bool
TestGuestLib(void)
{
   VMGuestLibError glError;
   Bool success = TRUE;
   uint32 cpuReservationMHz = 0;
   uint32 cpuLimitMHz = 0;
   uint32 cpuShares = 0;
   uint64 cpuUsedMs = 0;
   uint32 hostMHz = 0;
   uint32 memReservationMB = 0;
   uint32 memLimitMB = 0;
   uint32 memShares = 0;
   uint32 memMappedMB = 0;
   uint32 memActiveMB = 0;
   uint32 memOverheadMB = 0;
   uint32 memBalloonedMB = 0;
   uint32 memSwappedMB = 0;
   uint32 memSharedMB = 0;
   uint32 memSharedSavedMB = 0;
   uint32 memUsedMB = 0;
   uint64 elapsedMs = 0;
   VMSessionId sessionId = 0;
   char resourcePoolPath[512];
   size_t poolBufSize;

   /* Try to load the library. */
   glError = GuestLib_OpenHandle(&glHandle);
   if (glError != VMGUESTLIB_ERROR_SUCCESS) {
      printf("OpenHandle failed: %s\n", GuestLib_GetErrorText(glError));
      return FALSE;
   }

   /* Attempt to retrieve info from the host. */
   while (!done) {
      VMSessionId tmpSession;

      glError = GuestLib_UpdateInfo(glHandle);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("UpdateInfo failed: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }

      /* Retrieve and check the session ID */
      glError = GuestLib_GetSessionId(glHandle, &tmpSession);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get session ID: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }

      if (tmpSession == 0) {
         printf("Error: Got zero sessionId from GuestLib\n");
         success = FALSE;
         goto out;
      }
      if (sessionId == 0) {
         sessionId = tmpSession;
         printf("Initial session ID is 0x%"FMT64"x\n", sessionId);
      } else if (tmpSession != sessionId) {
         sessionId = tmpSession;
         printf("SESSION CHANGED: New session ID is 0x%"FMT64"x\n", sessionId);
      }

      /* Retrieve all the stats. */
      glError = GuestLib_GetCpuReservationMHz(glHandle, &cpuReservationMHz);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get CPU reservation: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetCpuLimitMHz(glHandle, &cpuLimitMHz);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get CPU limit: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetCpuShares(glHandle, &cpuShares);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get cpu shares: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetCpuUsedMs(glHandle, &cpuUsedMs);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get used ms: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetHostProcessorSpeed(glHandle, &hostMHz);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get host proc speed: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemReservationMB(glHandle, &memReservationMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get mem reservation: %s\n",
                GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemLimitMB(glHandle, &memLimitMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get mem limit: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemShares(glHandle, &memShares);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get mem shares: %s\n", GuestLib_GetErrorText(glError));
         if (glError == VMGUESTLIB_ERROR_NOT_AVAILABLE) {
            memShares = 0;
            printf("Skipping mem shares\n");
         } else {
            success = FALSE;
            goto out;
         }
      }
      glError = GuestLib_GetMemMappedMB(glHandle, &memMappedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get mapped mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemActiveMB(glHandle, &memActiveMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get active mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemOverheadMB(glHandle, &memOverheadMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get overhead mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemBalloonedMB(glHandle, &memBalloonedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get ballooned mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemSwappedMB(glHandle, &memSwappedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get swapped mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemSharedMB(glHandle, &memSharedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get swapped mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemSharedSavedMB(glHandle, &memSharedSavedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get swapped mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetMemUsedMB(glHandle, &memUsedMB);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get swapped mem: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      glError = GuestLib_GetElapsedMs(glHandle, &elapsedMs);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get elapsed ms: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }
      poolBufSize = sizeof resourcePoolPath;
      glError = GuestLib_GetResourcePoolPath(glHandle, &poolBufSize, resourcePoolPath);
      if (glError != VMGUESTLIB_ERROR_SUCCESS) {
         printf("Failed to get resource pool path: %s\n", GuestLib_GetErrorText(glError));
         success = FALSE;
         goto out;
      }

      /* Print the stats. */
      printf("cpuReservationMHz: %u\n"
             "cpuLimitMHz: %u\n"
             "cpuShares: %u\n"
             "cpuUsedMs: %"FMT64"u\n"
             "hostMHz: %u\n"
             "memReservationMB: %u\n"
             "memLimitMB: %u\n"
             "memShares: %u\n"
             "memMappedMB: %u\n"
             "memActiveMB: %u\n"
             "memOverheadMB: %u\n"
             "memBalloonedMB: %u\n"
             "memSwappedMB: %u\n"
             "memSharedMB: %u\n"
             "memSharedSavedMB: %u\n"
             "memUsedMB: %u\n"
             "elapsedMs: %"FMT64"u\n"
             "resourcePoolPath: '%s'\n",
             cpuReservationMHz, cpuLimitMHz,
             cpuShares, cpuUsedMs,
             hostMHz, memReservationMB,
             memLimitMB, memShares,
             memMappedMB, memActiveMB,
             memOverheadMB, memBalloonedMB,
             memSwappedMB, memSharedMB, 
             memSharedSavedMB, memUsedMB,
             elapsedMs, resourcePoolPath);

      /* Sleep for 1 second before repeating. */
      SLEEP(1);
   }

  out:
   glError = GuestLib_CloseHandle(glHandle);
   if (glError != VMGUESTLIB_ERROR_SUCCESS) {
      printf("Failed to CloseHandle: %s\n", GuestLib_GetErrorText(glError));
      success = FALSE;
   }

   return success;
}





int
main(int argc, char *argv[])
{
   /* Try to load the library. */
   if (!LoadFunctions()) {
      printf("GuestLibTest: Failed to load shared library\n");
      exit(1);
   }

   /* Test the VMware Guest API itself. */
   if (!TestGuestLib()) {
      printf("GuestLibTest: GuestLib testing failed\n");
      exit(1);
   }

#ifdef _WIN32
   if (!FreeLibrary(dlHandle)) {
      DWORD error = GetLastError();
      printf("Failed to FreeLibrary: %d\n", error);
      exit(1);
   }
#else
   if (dlclose(dlHandle)) {
      printf("dlclose failed\n");
      exit(1);
   }
#endif

   printf("Success!\n");
   exit(0);
}
