/*
 * Copyright 2007-2008.  VMware, Inc.  The use of this code is subject to 
 * your agreement to the terms of an applicable VMware license.
 */

/*
 * sharedMemApp.c --
 *
 *    A sample client-server application using the Virtual Machine
 *    Communication Interface (VMCI) shared memory API.
 *    Communicates current time at the server to the client via shared memory.
 */


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>

#ifdef _WIN32
#  include <windows.h>
#elif defined(__linux__) || defined(__APPLE__)
#  include <unistd.h>
#else
#   error "Unsupported platform"
#endif

#include "vmcilib.h"

#define HOST_SHM_SERVICE "HOST TIME"
#define SHARED_MEM_SIZE 128
#define SERVER_LOOP 10
#define CLIENT_LOOP 3

static char *devName = NULL;
static unsigned int isServer = 0;
static unsigned int isClient = 0;

static void ParseCommandLine(int argc, char *argv[]);
static void PrintUsageExit(const char *prog);
static void DoSharedMemServer(void);
static void DoSharedMemClient(void);
static void UpdateSharedMemoryServer(void *addr);


/*
 *----------------------------------------------------------------------------
 *
 * ParseCommandLine --
 *
 *      Parse the command line input parameters.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
ParseCommandLine(int argc,     // IN:
                 char *argv[]) // IN:
{
#ifdef _WIN32
   int index = 1;

   if (argc != 2 && argc != 4) {
      PrintUsageExit(argv[0]);
   }

   if (strcmp(argv[1], "-d") == 0) {
      devName = argv[2];
      index = 3;
   }

   if (strcmp(argv[index], "-s") == 0) {
      isServer = 1;
   } else if (strcmp(argv[index], "-c") == 0) {
      isClient = 1;
   } else {
      PrintUsageExit(argv[0]);
   }

#else // __linux__
   char *optstring = "d:sch";

   for (;;) {
      int c;

      c = getopt(argc, argv, optstring);
      if (c == -1) {
         break;
      }

      switch (c) {
         case 'd':
            devName = optarg;
            break;
         case 's':
            isServer = 1;
            break;
         case 'c':
            isClient = 1;
            break;
         case 'h':
         case '?':
         default:
            PrintUsageExit(argv[0]);
            break;
      }
   }
#endif
}


/*
 *----------------------------------------------------------------------------
 *
 * PrintUsageExit --
 *
 *      Prints program usage information and exits.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
PrintUsageExit(const char *prog) // IN:
{
   fprintf(stderr, "%s [-d <VMCI device>] -c|-s\n", prog);
   fprintf(stderr, "\t\tMust provide exactly one of -c OR -s.\n");
   fprintf(stderr, "%s -h:", prog);
   fprintf(stderr, "\tPrints this help message.\n");
   exit(-1);
}


int
main(int argc, char *argv[])
{

   ParseCommandLine(argc, argv);

   if ((!isServer && !isClient) || (isServer && isClient)) {
      PrintUsageExit(argv[0]);
   }

   if (VMCI_InitApp(devName) != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to init VMCI library.\n");
      exit(-2);
   }

   if (isServer) {
      DoSharedMemServer();
   } else {
      DoSharedMemClient();
   }

   VMCI_CleanupApp();

   return 0;
}

/*
 *-----------------------------------------------------------------------------
 *
 * DoSharedMemServer --
 *
 *      Implements the server side of the sample application. The server
 *      creates a shared memory segment and updates it with the current time
 *      every 1 second.
 *
 *      Uses the discovery service API to register the server handle under a
 *      known name.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
DoSharedMemServer(void)
{
   int rv;
   VMCIHandle serverHandle;
   void *addr;
   VMCIHandle publicGroupHandle;
   int j;

   serverHandle = VMCISharedMem_Create(SHARED_MEM_SIZE, &addr);
   if (VMCI_HANDLE_EQUAL(serverHandle, VMCI_INVALID_HANDLE)) {
      fprintf(stderr, "Failed to create server shared memory region.\n");
      return;
   }
   memset(addr, 0, SHARED_MEM_SIZE);

   rv = VMCIDs_Lookup(VMCI_PUBLIC_GROUP_NAME, &publicGroupHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to lookup public group handle.\n");
      goto out;
   }

   rv = VMCIDs_Register(HOST_SHM_SERVICE, serverHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to register discovery service. rv = %d\n", rv);
      goto out;
   }

   printf("Running shared memory server for %d seconds.\n", SERVER_LOOP);
   for (j = 0; j < SERVER_LOOP; j++) {
      /* Server loop */

      UpdateSharedMemoryServer(addr);
#ifdef _WIN32
      Sleep(1000);
#else // __linux__
      sleep(1);
#endif
      fprintf(stderr, "%d.", j);
   }
   fprintf(stderr, "\n");

   VMCIDs_Unregister(HOST_SHM_SERVICE);
out:
   VMCISharedMem_Detach(serverHandle);
}


/*
 *-----------------------------------------------------------------------------
 *
 * UpdateSharedMemoryServer --
 *
 *      Helper routine to update the shared memory region with current time.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
UpdateSharedMemoryServer(void *addr) // IN: Address of shared memory region
{
  struct tm *tmPtr;
  time_t locTime;
  char buf[SHARED_MEM_SIZE];
  int rv;

  locTime = time(NULL);
  tmPtr = localtime(&locTime);

  rv = strftime(buf, sizeof buf - 1, "%a %b %d %H:%M:%S %Z %Y", tmPtr);
  memcpy(addr, buf, rv);
}


/*
 *-----------------------------------------------------------------------------
 *
 * DoSharedMemClient --
 *
 *      Implements the client side of the sample application. The client
 *      creates attaches to the shared memory segment of the server, reads and
 *      prints the content of the shared memory segment.
 *
 *      Uses the discovery service API to lookup the server's handle.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
DoSharedMemClient(void)
{
   int rv;
   VMCIHandle serverHandle;
   void *addr;
   uint32 memSize;
   char *c;
   int j;

   rv = VMCIDs_Lookup(HOST_SHM_SERVICE, &serverHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to lookup in discovery service.\n");
      return;
   }

   rv = VMCISharedMem_Query(serverHandle, &memSize);
   if (rv < VMCI_SUCCESS) {
      fprintf(stderr, "Failed to query size of shared memory region.\n");
      return;
   }
   if (memSize != SHARED_MEM_SIZE) {
      fprintf(stderr, "Got wrong size of shared memory region.\n");
      fprintf(stderr, "Should be %u, got %u\n", SHARED_MEM_SIZE, memSize);
      return;
   }

   rv = VMCISharedMem_Attach(serverHandle, &addr, &memSize);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to attach to shared memory region. rv = %d\n", rv);
      return;
   }

   c = (char *) addr;
   for (j = 0; j < CLIENT_LOOP; j++) {
      /* Client loop */
      int i;

      for (i = 0; i < SHARED_MEM_SIZE; i++) {
         if (!c[i]) {
            break;
         }
         printf("%c", c[i]);
      }
      printf("\n");
#ifdef _WIN32
      Sleep(1000);
#else // __linux__
      sleep(1);
#endif
   }
   VMCISharedMem_Detach(serverHandle);
}
